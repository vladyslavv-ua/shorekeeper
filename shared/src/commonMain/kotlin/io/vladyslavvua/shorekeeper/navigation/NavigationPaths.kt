package io.vladyslavvua.shorekeeper.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationPaths : NavKey {

    @Serializable
    object Welcome : NavigationPaths {

    }

    @Serializable
    data object Shore : NavigationPaths {
        @Serializable
        data class OpenedShore(val shoreId: Long) : NavigationPaths

        @Serializable
        data class EditShore(val shoreId: Long) : NavigationPaths
    }


    @Serializable
    object Settings : NavigationPaths

    @Serializable
    object OpenCef : NavigationPaths
}