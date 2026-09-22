package me.gblrod.mavira.features.settings.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import me.gblrod.mavira.R
import me.gblrod.mavira.features.game.difficulty.model.DifficultyOptions
import me.gblrod.mavira.features.settings.model.SettingsItemData
import me.gblrod.mavira.ui.theme.LightGreen
import me.gblrod.mavira.ui.theme.LightRed

@Composable
fun GameplaySettingsCard(
    vibrationEnabled: Boolean,
    onVibrationChanged: (Boolean) -> Unit,
    difficulty: DifficultyOptions,
    onDifficultyClick: () -> Unit
) {
    SettingsCard(
        icon = Icons.Default.SportsEsports,
        title = stringResource(id = R.string.settings_gameplay_label),
        items = listOf(
            SettingsItemData(
                title = stringResource(id = R.string.settings_difficulty_label),
                description = stringResource(id = difficulty.label),
                icon = difficulty.icon,
                onClick = { onDifficultyClick() },
                trailingContent = { SettingsChevron() }
            ),
            SettingsItemData(
                title = stringResource(id = R.string.settings_vibration_label),
                description = stringResource(id = R.string.settings_vibration_description),
                icon = Icons.Default.Vibration,
                onClick = null,
                trailingContent = {
                    Switch(
                        checked = vibrationEnabled,
                        onCheckedChange = { onVibrationChanged(it) },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = LightGreen,
                            checkedThumbColor = Color.White,
                            uncheckedBorderColor = LightRed,
                            uncheckedThumbColor = LightRed
                        )
                    )
                }
            )
        )
    )
}