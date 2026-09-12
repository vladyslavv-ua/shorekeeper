package io.vladyslavvua.shorekeeper.shore.util

import io.vladyslavvua.shorekeeper.shore.entity.Shore
import kotlinx.serialization.json.Json
import java.io.File

fun validateShore(path: String): Shore? {
    val json = Json {
        ignoreUnknownKeys = true
    }
    val projectPath = File(path)
    if (!projectPath.exists()) {
        return null
    }

    val config = File("${projectPath.absolutePath}/shore.json")
    val views = File("${projectPath.absolutePath}/views")
    val queries = File("${projectPath.absolutePath}/queries")
    if (!views.exists() || !queries.exists() || !config.exists()) {
        return null
    }

    try {
        val parsedConfig = json.decodeFromString<Shore>(config.readText())
        return parsedConfig
    } catch (e: Exception) {
        return null
    }

    return null
}