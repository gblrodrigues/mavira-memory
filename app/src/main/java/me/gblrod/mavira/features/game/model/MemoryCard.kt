package me.gblrod.mavira.features.game.model

data class MemoryCard(
    val id: Int,
    val isRevealed: Boolean = false,
    val isMatched: Boolean = false,
    val isGlowing: Boolean = false,
    val symbol: MemorySymbol
)