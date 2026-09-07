package io.vladyslavvua.shorekeeper.jcef.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class JsToKotlinRequest(
    val method: String,
    val params: Map<String, JsonElement>
)
