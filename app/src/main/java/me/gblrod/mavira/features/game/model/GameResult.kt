package me.gblrod.mavira.features.game.model

import me.gblrod.mavira.features.game.difficulty.model.DifficultyOptions

data class GameResult(
    val id: Long = 0,
    val moves: Int,
    val time: Long,
    val playedAt: Long,
    val difficulty: DifficultyOptions
)