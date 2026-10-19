package me.gblrod.mavira.data.datastore.model

import java.time.LocalDate

sealed interface ChallengeDateState {
    data object Loading : ChallengeDateState
    data class Loaded(val lastAttemptDate: LocalDate?) : ChallengeDateState
}