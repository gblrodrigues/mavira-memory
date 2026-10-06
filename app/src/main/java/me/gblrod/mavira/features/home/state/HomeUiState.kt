package me.gblrod.mavira.features.home.state

data class HomeUiState(
    val isChallengeAvailable: Boolean? = null,
    val timeUntilChallenge: Long = 0L
)