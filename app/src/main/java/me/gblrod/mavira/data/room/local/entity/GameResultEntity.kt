package me.gblrod.mavira.data.room.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_results")
data class GameResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val moves: Int,
    val time: Long,
    val playedAt: Long
)