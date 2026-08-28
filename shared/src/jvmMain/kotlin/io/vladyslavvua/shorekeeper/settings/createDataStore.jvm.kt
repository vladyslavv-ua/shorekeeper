package io.vladyslavvua.shorekeeper.settings

import androidx.datastore.core.DataStore
import androidx.datastore.core.FileStorage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesFileSerializer
import java.io.File

actual fun createDataStore(): DataStore<ShorekeeperSettings> = createDataStore(
    storage = FileStorage(
        serializer = SettingsSerializer,
        produceFile = { File(System.getProperty("./"), "shorekeeper.json") }
    )
)