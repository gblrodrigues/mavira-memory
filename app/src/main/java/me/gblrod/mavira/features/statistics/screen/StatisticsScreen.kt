package me.gblrod.mavira.features.statistics.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.gblrod.mavira.R
import me.gblrod.mavira.features.statistics.components.StatisticsOverview
import me.gblrod.mavira.features.statistics.components.StatisticsSectionHeader
import me.gblrod.mavira.features.statistics.components.statisticsRecentGames
import me.gblrod.mavira.features.statistics.viewmodel.StatisticsViewModel

@Composable
fun StatisticsScreen(
    statisticsViewModel: StatisticsViewModel,
    onNavigateGameScreen: () -> Unit
) {
    val uiState by statisticsViewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            StatisticsSectionHeader(
                title = stringResource(id = R.string.statistics_title),
                description = stringResource(id = R.string.statistics_description)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            StatisticsOverview(uiState = uiState)
            Spacer(modifier = Modifier.height(24.dp))
        }

        statisticsRecentGames(
            games = uiState.recentGames,
            onNavigateGameScreen = { onNavigateGameScreen() }
        )
    }
}