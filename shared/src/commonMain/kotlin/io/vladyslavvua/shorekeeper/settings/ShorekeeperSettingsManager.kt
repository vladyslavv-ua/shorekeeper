package io.vladyslavvua.shorekeeper.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import io.vladyslavvua.shorekeeper.jcef.ShoreKeeperCefSettings
import io.vladyslavvua.shorekeeper.migrator.ShorekeeperMigrator
import io.vladyslavvua.shorekeeper.migrator.liquibase.LiquibaseMigrator
import io.vladyslavvua.shorekeeper.shore.entity.Migrator
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

    suspend fun getSettings(): ShorekeeperSettings {
        return dataStore.data.first()
    }

    suspend fun getMigratorPath(migrator: Migrator): ShorekeeperMigrator = when (migrator) {
        Migrator.FLYWAY -> LiquibaseMigrator(getSettings().liquibasePath) // todo rename
        Migrator.LIQUIBASE -> LiquibaseMigrator(getSettings().liquibasePath)
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