package me.gblrod.mavira.features.game.state

import me.gblrod.mavira.features.game.difficulty.model.DifficultyOptions
import me.gblrod.mavira.features.game.model.MemoryCard

data class GameUiState(
    val difficulty: DifficultyOptions = DifficultyOptions.EASY,
    val cards: List<MemoryCard> = emptyList(),
    val isCheckingPair: Boolean = false,
    val moveCount: Int = 0,
    val pairsFound: Int = 0,
    val timer: Long = 0,
    val isGameStarted: Boolean = false
)