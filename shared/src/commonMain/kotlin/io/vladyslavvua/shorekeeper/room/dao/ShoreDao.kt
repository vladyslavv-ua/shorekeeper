package io.vladyslavvua.shorekeeper.room.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import io.vladyslavvua.shorekeeper.room.entity.ShoreTable
import kotlinx.coroutines.flow.StateFlow

@Dao
interface ShoreDao {

    @Query("SELECT * FROM shore")
    suspend fun getAllShores(): List<ShoreTable>

    @Insert
    suspend fun insertShore(shore: ShoreTable)

    @Query("SELECT * FROM shore WHERE id = :shoreId")
    suspend fun getShoreById(shoreId: Long): ShoreTable?
}