package io.vladyslavvua.shorekeeper.shore.dbSettingsTable

import io.vladyslavvua.shorekeeper.SETTINGS_TABLE_NAME
import java.sql.Connection

fun setSettings(connection: Connection, settingKey: SettingsKeys, settingValue: String) {
    val checkIsSettingSet = "SELECT * FROM $SETTINGS_TABLE_NAME WHERE setting_key = ?"
    val statement = connection.prepareStatement(checkIsSettingSet)
    statement.setString(1, settingKey.name)
    val resultSet = statement.executeQuery()

    if (!resultSet.next()) {
        val insertSetting = "INSERT INTO $SETTINGS_TABLE_NAME (setting_key, setting_value) VALUES (?, ?)"
        val insertStatement = connection.prepareStatement(insertSetting)
        insertStatement.setString(1, settingKey.name)
        insertStatement.setString(2, settingValue)
        insertStatement.execute()
    } else {
        val updateSetting = "UPDATE $SETTINGS_TABLE_NAME SET setting_value = ? WHERE setting_key = ?"
        val updateStatement = connection.prepareStatement(updateSetting)
        updateStatement.setString(1, settingValue)
        updateStatement.setString(2, settingKey.name)
        updateStatement.execute()
    }
}