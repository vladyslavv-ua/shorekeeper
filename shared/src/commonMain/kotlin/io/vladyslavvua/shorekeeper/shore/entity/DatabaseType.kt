package io.vladyslavvua.shorekeeper.shore.entity

data class DatabaseType(
    val name: String,
    val jdbcTypeCode: Int,
    val nullable: Boolean
)
