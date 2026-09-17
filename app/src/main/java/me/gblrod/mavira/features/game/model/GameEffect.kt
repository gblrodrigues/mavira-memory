package me.gblrod.mavira.features.game.model

sealed interface GameEffect {
    data object PairMatched : GameEffect
    data object GameCompleted : GameEffect
}