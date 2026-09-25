package me.gblrod.mavira.features.game.engine.model

data class SelectionResult(
    val state: GameState,
    val accepted: Boolean,
    val pairReady: Boolean
)