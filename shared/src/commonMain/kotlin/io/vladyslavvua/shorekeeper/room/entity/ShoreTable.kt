package io.vladyslavvua.shorekeeper.room.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey


@Entity(tableName = "shore")
data class ShoreTable(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String,
    val path: String
)
