package io.vladyslavvua.shorekeeper.settings

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Storage
import androidx.datastore.preferences.core.Preferences

fun createDataStore(storage: Storage<ShorekeeperSettings>): DataStore<ShorekeeperSettings> =
    DataStoreFactory.create(storage = storage)

expect fun createDataStore(): DataStore<ShorekeeperSettings>