package io.vladyslavvua.shorekeeper.shore.entity

import kotlinx.serialization.Serializable

@Serializable
data class Shore(
    val name: String,
    val version: Int,
    val migrator: Migrator,
    val database: ShoreDbConnections,
    val viewSettings: ShoreViewSettings? = null
)