package me.gblrod.mavira.features.home.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector
import me.gblrod.mavira.R

enum class ButtonItems(
    val label: Int,
    val icon: ImageVector
) {
    PLAY(label = R.string.button_play, icon = Icons.Default.PlayArrow),
    CHALLENGE(label = R.string.button_challenge, icon = Icons.Default.Timer),
    STATS(label = R.string.button_stats, icon = Icons.Default.MilitaryTech),
    CONFIG(label = R.string.button_configs, icon = Icons.Default.Settings)
}