package io.vladyslavvua.shorekeeper.shore.entity

import io.ktor.http.content.EntityTagVersion
import kotlinx.serialization.Serializable

@Serializable
data class Shore(
    val shorekeeperVersion: Int,
    val name: String,
    val version: Int,
    val migrator: Migrator,
    val database: ShoreDbConnections,
    val viewSettings: ShoreViewSettings? = null
)