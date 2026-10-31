package me.gblrod.mavira.features.settings.components

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import me.gblrod.mavira.ui.theme.LightGreen
import me.gblrod.mavira.ui.theme.LightRed

@Composable
fun SettingsSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        checked = checked,
        onCheckedChange = { onCheckedChange(it) },
        colors = SwitchDefaults.colors(
            checkedTrackColor = LightGreen,
            checkedThumbColor = Color.White,
            uncheckedBorderColor = LightRed,
            uncheckedThumbColor = LightRed
        )
    )
}