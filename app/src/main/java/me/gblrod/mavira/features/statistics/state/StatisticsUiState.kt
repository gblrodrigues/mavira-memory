package me.gblrod.mavira.features.statistics.state

import me.gblrod.mavira.features.game.model.GameResult

data class StatisticsUiState(
    val gamesPlayed: Int = 0,
    val bestTime: Long? = null,
    val bestMoves: Int? = null,
    val recentGames: List<GameResult> = emptyList(),
    val averageTime: Long? = null
)