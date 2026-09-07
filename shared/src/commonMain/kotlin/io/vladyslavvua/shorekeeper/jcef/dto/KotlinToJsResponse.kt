package io.vladyslavvua.shorekeeper.jcef.dto

import kotlinx.serialization.Serializable

@Serializable
data class KotlinToJsResponse(
    val success: Boolean,
    val data: String? = null,
    val reason:FailureReason? = null,
){
    enum class FailureReason {
        INVALID_PARAMS,
        METHOD_NOT_FOUND,
    }
}
