package me.gblrod.mavira.features.game.model

data class MemoryCard(
    val id: Int,
    val item: MemoryItem,
    val isRevealed: Boolean = false,
    val isMatched: Boolean = false
)