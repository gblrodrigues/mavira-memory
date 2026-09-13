package me.gblrod.mavira.features.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.gblrod.mavira.R
import me.gblrod.mavira.features.statistics.state.StatisticsUiState
import me.gblrod.mavira.shared.extension.toTimerFormat

@Composable
fun StatisticsOverview(
    uiState: StatisticsUiState
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatisticsCardRow {
            StatisticsCard(
                modifier = Modifier.weight(1f),
                label = stringResource(id = R.string.statistics_games_played),
                value = uiState.gamesPlayed.toString(),
                icon = Icons.Default.SportsEsports
            )

            StatisticsCard(
                modifier = Modifier.weight(1f),
                label = stringResource(id = R.string.statistics_best_time),
                value = uiState.bestTime?.toTimerFormat() ?: "--:--",
                icon = Icons.Default.AccessTime
            )
        }

        StatisticsCardRow {
            StatisticsCard(
                modifier = Modifier.weight(1f),
                label = stringResource(id = R.string.statistics_best_moves),
                value = uiState.bestMoves?.toString() ?: "--",
                icon = Icons.Default.TouchApp
            )

            StatisticsCard(
                modifier = Modifier.weight(1f),
                label = stringResource(id = R.string.statistics_average_time),
                value = uiState.averageTime?.toTimerFormat() ?: "--:--",
                icon = Icons.Default.AccessTime
            )
        }
    }
}