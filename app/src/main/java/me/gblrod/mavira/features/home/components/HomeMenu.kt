package me.gblrod.mavira.features.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.gblrod.mavira.features.home.model.ButtonItems
import me.gblrod.mavira.shared.extension.toHoursMinutesSecondsFormat
import me.gblrod.mavira.ui.theme.LightBlue
import me.gblrod.mavira.ui.theme.LightRed

@Composable
fun HomeMenu(
    isChallengeAvailable: Boolean?,
    timeUntilAvailable: Long,
    onItemClick: (ButtonItems) -> Unit
) {
    ButtonItems.entries.forEach { item ->
        val isEnabled = when (item) {
            ButtonItems.CHALLENGE -> isChallengeAvailable != false
            else -> true
        }

        Button(
            onClick = { onItemClick(item) },
            enabled = isEnabled,
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = LightBlue,
                disabledContainerColor = Color.DarkGray
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(id = item.label),
                    color = Color.White
                )

                Spacer(modifier = Modifier.weight(1f))

                if (!isEnabled) {
                    ChallengeCooldownUi(
                        timeUntilAvailable = timeUntilAvailable
                    )
                }
            }
        }
    }
}

@Composable
private fun ChallengeCooldownUi(
    timeUntilAvailable: Long
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = Color.Black.copy(alpha = 0.2f),
        border = BorderStroke(
            width = 1.dp,
            color = LightRed
        )
    ) {
        Text(
            text = timeUntilAvailable.toHoursMinutesSecondsFormat(),
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 4.dp
            ),
            color = Color.White.copy(alpha = 0.8f),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}