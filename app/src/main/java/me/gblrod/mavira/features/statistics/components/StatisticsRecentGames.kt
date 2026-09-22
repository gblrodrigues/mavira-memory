package me.gblrod.mavira.features.statistics.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.gblrod.mavira.R
import me.gblrod.mavira.features.game.model.GameResult
import me.gblrod.mavira.features.statistics.config.StatisticsConfig.RECENT_GAMES_LIMIT
import me.gblrod.mavira.features.statistics.util.toStatisticsDate
import me.gblrod.mavira.features.statistics.util.toStatisticsTime
import me.gblrod.mavira.shared.extension.toTimerFormat

fun LazyListScope.statisticsRecentGames(
    games: List<GameResult>,
    onNavigateGameScreen: () -> Unit
) {
    item {
        StatisticsSectionHeader(
            title = stringResource(id = R.string.statistics_recent_game_label),
            description = stringResource(
                id = R.string.statistics_recent_game_description,
                RECENT_GAMES_LIMIT
            )
        )

        HorizontalDivider(
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 12.dp)
        )
    }

    if (games.isEmpty()) {
        item {
            StatisticsEmptyState(
                onClick = { onNavigateGameScreen() }
            )
        }
    } else {
        items(
            items = games,
            key = { game -> game.id }
        ) { recentGame ->
            Spacer(modifier = Modifier.height(8.dp))

            StatisticsRecentGameRow(
                difficultyTitle = stringResource(id = R.string.settings_difficulty_label),
                difficultyLabel = recentGame.difficulty,
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