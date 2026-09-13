package me.gblrod.mavira.features.game.model

data class GameResult(
    val id: Long = 0,
    val moves: Int,
    val time: Long,
    val playedAt: Long
)