package io.vladyslavvua.shorekeeper.room.repo

import io.vladyslavvua.shorekeeper.room.AppDb
import io.vladyslavvua.shorekeeper.room.entity.ShoreTable
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.Singleton

@Singleton
class ShoreRepo(
    db: AppDb
) {
    private val shoreDao = db.shoreDao()


    suspend fun getShores(): List<ShoreTable> {
        return shoreDao.getAllShores()
    }

    suspend fun insertShore(name: String, path: String) {
        shoreDao.insertShore(ShoreTable(null, name, path))
    }

    suspend fun getShoreById(shoreId: Long): ShoreTable? {
        return shoreDao.getShoreById(shoreId)
    }
}