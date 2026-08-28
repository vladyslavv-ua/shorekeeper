package io.vladyslavvua.shorekeeper.jcef

import org.koin.dsl.module

val jcefModule = module {
    single { JcefSettingsProvider() }
    single { JcefManager(get()) }
}