package me.gblrod.mavira.features.game.engine.model

import me.gblrod.mavira.features.game.model.MemoryCard

data class GameState(
    val cards: List<MemoryCard> = emptyList(),
    val moveCount: Int = 0,
    val pairsFound: Int = 0,
    val pairsTotal: Int = 0
) {
    val isCompleted: Boolean get() = pairsTotal > 0 && pairsFound == pairsTotal
}