package me.gblrod.mavira.features.settings.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.ui.graphics.vector.ImageVector
import me.gblrod.mavira.R

enum class ThemeOptions(
    val label: Int,
    val icon: ImageVector
) {
    SYSTEM(label = R.string.theme_system, icon = Icons.Default.BrightnessAuto),
    LIGHT(label = R.string.theme_lightmode, Icons.Default.LightMode),
    DARK(label = R.string.theme_darkmode, Icons.Default.DarkMode)
}