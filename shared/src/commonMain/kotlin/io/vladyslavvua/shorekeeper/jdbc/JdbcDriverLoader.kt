package io.vladyslavvua.shorekeeper.jdbc

import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.sql.Driver
import java.sql.DriverManager
import java.util.jar.JarFile

object JdbcDriverLoader {

    fun loadAll(folder: File): List<Driver> {
        require(folder.isDirectory) { "Not a directory: $folder" }

        val jars = folder.listFiles { f -> f.extension.equals("jar", ignoreCase = true) }
            ?.toList() ?: emptyList()

        if (jars.isEmpty()) return emptyList()

        val urls: Array<URL> = jars.map { it.toURI().toURL() }.toTypedArray()
        val ucl = URLClassLoader(urls, Thread.currentThread().contextClassLoader)

        val driverClassNames = linkedSetOf<String>()


        for (jar in jars) {
            driverClassNames += readServiceLoaderEntries(jar)
        }

        // 2) fallback: якщо десь немає SPI-файлу — скануємо всі .class у jar-і
        //    і перевіряємо, чи implements java.sql.Driver
        if (driverClassNames.isEmpty()) {
            for (jar in jars) {
                driverClassNames += scanJarForDriverClasses(jar, ucl)
            }
        }

        val registered = mutableListOf<Driver>()
        for (className in driverClassNames) {
            try {
                val driverInstance = Class.forName(className, true, ucl)
                    .getDeclaredConstructor()
                    .newInstance() as Driver
                val shim = DriverShim(driverInstance)
                DriverManager.registerDriver(shim)
                registered += shim
                println("Registered driver: $className")
            } catch (e: Exception) {
                println("Failed to load driver $className: ${e.message}")
            }
        }

        return registered
    }

    private fun readServiceLoaderEntries(jar: File): List<String> {
        val path = "META-INF/services/java.sql.Driver"
        JarFile(jar).use { jf ->
            val entry = jf.getJarEntry(path) ?: return emptyList()
            jf.getInputStream(entry).bufferedReader().use { reader ->
                return reader.readLines()
                    .map { it.substringBefore('#').trim() }
                    .filter { it.isNotEmpty() }
            }
        }
    }

    private fun scanJarForDriverClasses(jar: File, ucl: URLClassLoader): List<String> {
        val result = mutableListOf<String>()
        JarFile(jar).use { jf ->
            jf.entries().asSequence()
                .filter { it.name.endsWith(".class") && !it.name.contains("$") }
                .forEach { entry ->
                    val className = entry.name
                        .removeSuffix(".class")
                        .replace('/', '.')
                    try {
                        val clazz = Class.forName(className, false, ucl)
                        if (Driver::class.java.isAssignableFrom(clazz) &&
                            !clazz.isInterface &&
                            !java.lang.reflect.Modifier.isAbstract(clazz.modifiers)
                        ) {
                            result += className
                        }
                    } catch (_: Throwable) {
                        // деякі класи не завантажуються ізольовано (залежності, native і т.д.) — ігноруємо
                    }
                }
        }
        return result
    }
}
