package io.vladyslavvua.shorekeeper.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import io.vladyslavvua.shorekeeper.jcef.ShoreKeeperCefSettings
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.Singleton

@Singleton
class ShorekeeperSettingsManager(
    val dataStore: DataStore<ShorekeeperSettings>
) {


    suspend fun updateSettings(newSettings: ShorekeeperSettings) {
        dataStore.updateData { settings ->
            newSettings
        }
    }

    suspend fun getCefSettings(): ShoreKeeperCefSettings {
        val settings = dataStore.data.first()
        return ShoreKeeperCefSettings(
            settings.cefPath,
            settings.cefHelper,
            settings.cefCachePath
        )
    }

}