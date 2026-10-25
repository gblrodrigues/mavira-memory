package me.gblrod.mavira.features.settings.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import me.gblrod.mavira.R
import me.gblrod.mavira.features.settings.model.SettingsItemData
import me.gblrod.mavira.ui.theme.LightGreen
import me.gblrod.mavira.ui.theme.LightRed

@Composable
fun AudioSettingsCard(
    musicBackgroundEnabled: Boolean,
    onMusicBackgroundChanged: (Boolean) -> Unit
) {
    SettingsCard(
        icon = Icons.AutoMirrored.Filled.VolumeUp,
        title = stringResource(id = R.string.settings_audio_label),
        items = listOf(
            SettingsItemData(
                title = stringResource(id = R.string.settings_background_music_label),
                description = stringResource(id = R.string.settings_background_music_description),
                icon = if (musicBackgroundEnabled) Icons.Default.MusicNote else Icons.Default.MusicOff,
                onClick = null,
                trailingContent = {
                    Switch(
                        checked = musicBackgroundEnabled,
                        onCheckedChange = { onMusicBackgroundChanged(it) },
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