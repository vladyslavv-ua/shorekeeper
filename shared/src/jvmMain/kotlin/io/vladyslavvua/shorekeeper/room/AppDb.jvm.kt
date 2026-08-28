package io.vladyslavvua.shorekeeper.room

import androidx.room3.Room
import androidx.room3.RoomDatabase
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDb> {
    val dbFile = File(System.getProperty("./"), "shorekeeper.db")
    return Room.databaseBuilder<AppDb>(
        name = dbFile.absolutePath,
    )
}