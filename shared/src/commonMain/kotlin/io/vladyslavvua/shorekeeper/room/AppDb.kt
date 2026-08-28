package io.vladyslavvua.shorekeeper.room

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import io.vladyslavvua.shorekeeper.room.dao.ShoreDao
import io.vladyslavvua.shorekeeper.room.entity.ShoreTable

@Database(entities = [ShoreTable::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDb : RoomDatabase() {
    abstract fun shoreDao(): ShoreDao
}

// The Room compiler generates the `actual` implementations.
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDb> {
    override fun initialize(): AppDb
}

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDb>