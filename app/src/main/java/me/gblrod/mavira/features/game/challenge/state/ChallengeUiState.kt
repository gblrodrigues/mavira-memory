package me.gblrod.mavira.features.game.challenge.state

import me.gblrod.mavira.features.game.config.ChallengeConfig
import me.gblrod.mavira.features.game.difficulty.model.DifficultyOptions
import me.gblrod.mavira.features.game.model.MemoryCard

data class ChallengeUiState(
    val difficulty: DifficultyOptions = DifficultyOptions.MEDIUM,
    val cards: List<MemoryCard> = emptyList(),
    val isCheckingPair: Boolean = false,
    val moveCount: Int = 0,
    val pairsFound: Int = 0,
    val timer: Long = ChallengeConfig.TIME_LIMIT_SECONDS,
    val isGameStarted: Boolean = false,
    val isCompleted: Boolean = false,
    val isFailed: Boolean = false
)