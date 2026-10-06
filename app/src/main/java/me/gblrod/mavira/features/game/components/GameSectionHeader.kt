package me.gblrod.mavira.features.game.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.gblrod.mavira.R
import me.gblrod.mavira.features.game.difficulty.model.DifficultyOptions
import me.gblrod.mavira.shared.extension.toTimerFormat
import me.gblrod.mavira.ui.theme.DarkGreen
import me.gblrod.mavira.ui.theme.Gold

@Composable
fun GameSectionHeader(
    modifier: Modifier = Modifier,
    moveCount: Int,
    pairsFound: Int,
    pairsTotal: Int,
    timer: Long = 0L,
    difficulty: DifficultyOptions,
    difficultyRowModifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CardItem(
            color = DarkGreen,
            text = R.string.game_movements,
            icon = Icons.Default.TouchApp,
            value = moveCount,
            modifier = Modifier.weight(1f)
        )

        CardItem(
            color = Gold,
            text = R.string.game_pairs,
            icon = Icons.Default.Style,
            value = pairsFound,
            secondaryValue = pairsTotal,
            modifier = Modifier.weight(1f)
        )

        CardItem(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            text = R.string.game_timer,
            icon = Icons.Default.AccessTime,
            secondaryValueText = timer.toTimerFormat(),
            modifier = Modifier.weight(1f)
        )
    }

    Row(
        modifier = difficultyRowModifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = difficulty.icon,
            contentDescription = null,
            tint = difficulty.color
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = stringResource(id = difficulty.label),
            color = difficulty.color,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
private fun CardItem(
    modifier: Modifier = Modifier,
    color: Color,
    icon: ImageVector,
    text: Int,
    value: Int? = null,
    secondaryValue: Int? = null,
    secondaryValueText: String? = null
) {
    Card(
        colors = CardDefaults.cardColors(contentColor = color, containerColor = Color.Transparent),
        border = BorderStroke(width = 1.dp, color = color),
        shape = MaterialTheme.shapes.large,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )

                Text(
                    text = when {
                        value != null && secondaryValue != null -> stringResource(id = text, value, secondaryValue)
                        secondaryValueText != null -> stringResource(id = text, secondaryValueText)
                        value != null -> stringResource(id = text, value)
                        else -> stringResource(id = text)
                    }
                )
            }
        }
    }
}