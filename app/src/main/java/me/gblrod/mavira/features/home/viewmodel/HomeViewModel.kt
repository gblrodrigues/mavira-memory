package me.gblrod.mavira.features.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import me.gblrod.mavira.data.datastore.repository.ChallengeRepository
import me.gblrod.mavira.features.home.state.HomeUiState
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    challengeRepository: ChallengeRepository
) : ViewModel() {

    private val currentDateTime = flow {
        while (true) {
            emit(value = LocalDateTime.now())
            delay(duration = 1_000.milliseconds)
        }
    }

    val uiState = combine(
        flow = challengeRepository.lastChallengeDate,
        flow2 = currentDateTime
    ) { lastChallengeDate, currentDateTime ->

        val today = currentDateTime.toLocalDate()

        val nextDay = today
            .plusDays(1)
            .atStartOfDay()

        HomeUiState(
            isChallengeAvailable = lastChallengeDate != today,
            timeUntilChallenge = Duration
                .between(currentDateTime, nextDay)
                .seconds
                .coerceAtLeast(minimumValue = 0L)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = HomeUiState()
    )
}