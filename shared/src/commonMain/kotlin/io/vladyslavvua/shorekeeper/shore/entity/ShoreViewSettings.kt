package io.vladyslavvua.shorekeeper.shore.entity

import kotlinx.serialization.Serializable

@Serializable
data class ShoreViewSettings(
    val entryPoint: String = "index.html"
)
