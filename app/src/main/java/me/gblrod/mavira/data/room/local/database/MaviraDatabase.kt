package me.gblrod.mavira.data.room.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import me.gblrod.mavira.data.room.local.dao.GameResultDao
import me.gblrod.mavira.data.room.local.entity.GameResultEntity

@Database(
    entities = [GameResultEntity::class],
    version = 1,
    exportSchema = false
)

abstract class MaviraDatabase : RoomDatabase() {
    abstract fun gameResultDao(): GameResultDao
}