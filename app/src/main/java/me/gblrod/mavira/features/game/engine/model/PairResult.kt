package me.gblrod.mavira.features.game.engine.model

data class PairResult(
    val state: GameState,
    val firstCardId: Int,
    val secondCardId: Int,
    val isMatch: Boolean,
    val isComplete: Boolean
)