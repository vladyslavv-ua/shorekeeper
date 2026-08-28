package io.vladyslavvua.shorekeeper.shore.entity

import kotlinx.serialization.Serializable

@Serializable
data class ShoreDbConnections(
    val connectionString: String,
    val authType: AuthType,
    val credentials: Credentials? = null

){
    @Serializable
    data class Credentials(val username: String, val password: String)
}
