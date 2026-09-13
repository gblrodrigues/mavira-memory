package me.gblrod.mavira.features.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.gblrod.mavira.R
import me.gblrod.mavira.features.game.model.GameResult
import me.gblrod.mavira.features.statistics.util.toStatisticsDate
import me.gblrod.mavira.features.statistics.util.toStatisticsTime
import me.gblrod.mavira.shared.extension.toTimerFormat

@Composable
fun StatisticsRecentGames(
    games: List<GameResult>,
    onNavigateGameScreen: () -> Unit
) {
    StatisticsSectionHeader(
        title = stringResource(id = R.string.statistics_recent_game_label),
        description = stringResource(id = R.string.statistics_recent_game_description)
    )

    HorizontalDivider(
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 12.dp)
    )

    if (games.isEmpty()) {
        StatisticsEmptyState(
            onClick = { onNavigateGameScreen() }
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = games,
                key = { game -> game.id }
            ) { recentGame ->
                StatisticsRecentGameRow(
                    date = recentGame.playedAt.toStatisticsDate(),
                    movesLabel = stringResource(id = R.string.statistics_plays),
                    timeLabel = stringResource(id = R.string.statistics_timer),
                    timePlayed = recentGame.playedAt.toStatisticsTime(),
                    moves = recentGame.moves.toString(),
                    time = recentGame.time.toTimerFormat()
                )
            }
        }
    }

}