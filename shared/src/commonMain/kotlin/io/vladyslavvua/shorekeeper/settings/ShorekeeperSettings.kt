package io.vladyslavvua.shorekeeper.settings

import kotlinx.serialization.Serializable

@Serializable
data class ShorekeeperSettings(
    val cefPath: String = "",
    val cefHelper: String = "",
    val cefCachePath: String = "",
    val liquibasePath: String = "",
    val flywayPath: String = ""
) {
}

