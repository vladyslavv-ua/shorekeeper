package io.vladyslavvua.shorekeeper.shore.entity

import kotlinx.serialization.Serializable

@Serializable
data class ShoreQuery(
    val name: String,
    val query: String,
    val parameters: List<ShoreQuery_Parameter>
){
    @Serializable
    data class ShoreQuery_Parameter(
        val name: String,
        val type: ShoreQuery_Parameter_Type,
        val required: Boolean
    ){
        @Serializable
        enum class ShoreQuery_Parameter_Type{
            STRING,
            INT,
            FLOAT,
            BOOLEAN
        }
    }
}
