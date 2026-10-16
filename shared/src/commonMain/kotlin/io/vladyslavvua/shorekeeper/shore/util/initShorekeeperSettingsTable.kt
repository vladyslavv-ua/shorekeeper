package io.vladyslavvua.shorekeeper.shore.util

import io.vladyslavvua.shorekeeper.SETTINGS_TABLE_NAME
import java.sql.Connection
import java.sql.SQLException
import java.util.*


fun initShorekeeperSettingsTable(connection: Connection) {
    if (!tableExists(connection, SETTINGS_TABLE_NAME)) {
        connection.createStatement().use { statement ->
            val sql = "CREATE TABLE $SETTINGS_TABLE_NAME (" +
                    "setting_key VARCHAR(255) NOT NULL, " +
                    "setting_value VARCHAR(4000), " +
                    "CONSTRAINT pk_app_settings PRIMARY KEY (setting_key))"
            statement.executeUpdate(sql)
        }
    }
}

@Throws(SQLException::class)
private fun tableExists(connection: Connection, tableName: String): Boolean {
    val metaData = connection.metaData
    // Деякі СУБД повертають назви у верхньому регістрі
    val searchName: String? =
        if (metaData.storesUpperCaseIdentifiers()) tableName.uppercase(Locale.getDefault()) else tableName

    metaData.getTables(null, null, searchName, arrayOf("TABLE")).use { resultSet ->
        return resultSet.next()
    }
}