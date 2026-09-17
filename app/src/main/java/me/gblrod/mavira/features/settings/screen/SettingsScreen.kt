package me.gblrod.mavira.features.settings.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.gblrod.mavira.R
import me.gblrod.mavira.features.settings.components.SettingsCard
import me.gblrod.mavira.features.settings.components.SettingsHeader
import me.gblrod.mavira.features.settings.components.SettingsLanguageDialog
import me.gblrod.mavira.features.settings.components.SettingsThemeDialog
import me.gblrod.mavira.features.settings.model.SettingsItemData
import me.gblrod.mavira.features.settings.viewmodel.SettingsViewModel
import me.gblrod.mavira.ui.theme.LightGreen
import me.gblrod.mavira.ui.theme.LightRed

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel
) {
    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SettingsHeader(
            title = stringResource(id = R.string.settings_preferences_label),
            description = stringResource(id = R.string.settings_preferences_description),
            icon = Icons.Default.Settings
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingsCard(
            icon = Icons.Default.Brush,
            title = stringResource(id = R.string.settings_appearance_label),
            items = listOf(
                SettingsItemData(
                    title = stringResource(id = R.string.settings_theme_label),
                    description = stringResource(id = uiState.themeOptions.label),
                    icon = uiState.themeOptions.icon,
                    onClick = { showThemeDialog = true },
                    trailingContent = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                ),
                SettingsItemData(
                    title = stringResource(id = R.string.settings_language_label),
                    description = stringResource(id = uiState.languageOptions.label),
                    icon = Icons.Default.Language,
                    onClick = { showLanguageDialog = true },
                    trailingContent = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingsCard(
            icon = Icons.Default.Vibration,
            title = stringResource(id = R.string.settings_haptics_label),
            items = listOf(
                SettingsItemData(
                    title = stringResource(id = R.string.settings_vibration_label),
                    description = stringResource(id = R.string.settings_vibration_description),
                    icon = Icons.Default.Vibration,
                    onClick = null,
                    trailingContent = {
                        Switch(
                            checked = uiState.vibrationEnabled,
                            onCheckedChange = { settingsViewModel.setVibration(enabled = it) },
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

        if (showThemeDialog) {
            SettingsThemeDialog(
                selectedTheme = uiState.themeOptions,
                onThemeSelected = { settingsViewModel.setTheme(theme = it) },
                onDismiss = { showThemeDialog = false }
            )
        }

        if (showLanguageDialog) {
            SettingsLanguageDialog(
                selectedLanguage = uiState.languageOptions,
                onLanguageSelected = { settingsViewModel.setLanguage(language = it) },
                onDismiss = { showLanguageDialog = false }
            )
        }
    }
}