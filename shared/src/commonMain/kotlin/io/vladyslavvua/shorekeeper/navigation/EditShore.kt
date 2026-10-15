package io.vladyslavvua.shorekeeper.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface EditShore : NavigationPaths {

    @Serializable
    sealed interface Tab: NavigationPaths {
        @Serializable
        data object QuickSetup : Tab

        @Serializable
        data object Cef : Tab
    }


}