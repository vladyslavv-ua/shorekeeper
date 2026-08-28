package io.vladyslavvua.shorekeeper.di

import androidx.datastore.core.DataStore
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.vladyslavvua.shorekeeper.jcef.jcefModule
import io.vladyslavvua.shorekeeper.room.AppDb
import io.vladyslavvua.shorekeeper.room.getDatabaseBuilder
import io.vladyslavvua.shorekeeper.settings.ShorekeeperSettings
import io.vladyslavvua.shorekeeper.settings.createDataStore
import kotlinx.coroutines.Dispatchers
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.plugin.module.dsl.startKoin

fun initKoin() = startKoin<ShorekeeperApplication> {
    printLogger(Level.DEBUG)

    modules(
        module {
//        single<CefClient> { CefManager.in.createClient() }
            single<DataStore<ShorekeeperSettings>> { createDataStore() }
            single<AppDb> {
                getDatabaseBuilder().setDriver(BundledSQLiteDriver())
                    .setQueryCoroutineContext(Dispatchers.IO)
                    .build()
            }
        },
        jcefModule
    )

}
