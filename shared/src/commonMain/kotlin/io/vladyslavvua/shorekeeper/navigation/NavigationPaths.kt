package io.vladyslavvua.shorekeeper.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationPaths {

    @Serializable
    object Welcome : NavigationPaths() {

        @Serializable
        data class Shore(val shoreId: Long) : NavigationPaths()
    }

    @Serializable
    object Settings : NavigationPaths()
}