package me.gblrod.mavira.features.game.state

import me.gblrod.mavira.features.game.model.MemoryCard
import me.gblrod.mavira.features.game.model.createDeck

data class GameUiState(
    val cards: List<MemoryCard> = createDeck(),
    val isCheckingPair: Boolean = false,
    val moveCount: Int = 0,
    val pairsFound: Int = 0,
    val pairsTotal: Int = 8,
    val gridSize: Int = 4,
    val timer: Long = 0
)