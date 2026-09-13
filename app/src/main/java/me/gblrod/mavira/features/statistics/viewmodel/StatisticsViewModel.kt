package me.gblrod.mavira.features.statistics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.gblrod.mavira.data.room.repository.GameRepository
import me.gblrod.mavira.features.statistics.state.StatisticsUiState
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    gameRepository: GameRepository
) : ViewModel() {
    val uiState = gameRepository
        .observeResults()
        .map { results ->
            StatisticsUiState(
                gamesPlayed = results.size,
                bestTime = results.minOfOrNull { it.time },
                bestMoves = results.minOfOrNull { it.moves },
                recentGames = results,
                averageTime = results
                    .map { it.time }
                    .takeIf { it.isNotEmpty() }
                    ?.average()
                    ?.toLong()
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = StatisticsUiState()
        )
}