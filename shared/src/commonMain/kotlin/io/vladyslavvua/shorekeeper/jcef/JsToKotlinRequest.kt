package io.vladyslavvua.shorekeeper.jcef

import kotlinx.serialization.Serializable

@Serializable
data class JsToKotlinRequest(
    val method: String,
    val params: Map<String, String>
)
