package io.vladyslavvua.shorekeeper

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.vladyslavvua.shorekeeper.di.initKoin
import io.vladyslavvua.shorekeeper.jdbc.JdbcDriverLoader
import java.io.File
import java.sql.DriverManager

fun main() {
    initKoin()

    JdbcDriverLoader.loadAll(File("./jdbc-drivers"))


    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Shorekeeper",
        ) {
            App()
        }
    }
}

