package me.gblrod.mavira.features.settings.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.gblrod.mavira.R
import me.gblrod.mavira.features.settings.components.AppearanceSettingsCard
import me.gblrod.mavira.features.settings.components.HapticsSettingsCard
import me.gblrod.mavira.features.settings.components.SettingsHeader
import me.gblrod.mavira.features.settings.components.SettingsLanguageDialog
import me.gblrod.mavira.features.settings.components.SettingsThemeDialog
import me.gblrod.mavira.features.settings.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel
) {
    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SettingsHeader(
                title = stringResource(id = R.string.settings_preferences_label),
                description = stringResource(id = R.string.settings_preferences_description),
                icon = Icons.Default.Settings
            )
        }

        item {
            AppearanceSettingsCard(
                theme = uiState.themeOptions,
                language = uiState.languageOptions,
                onThemeClick = { showThemeDialog = true },
                onLanguageClick = { showLanguageDialog = true }
            )
        }

        item {
            HapticsSettingsCard(
                vibrationEnabled = uiState.vibrationEnabled,
                onVibrationChanged = { settingsViewModel.setVibration(enabled = it) }
            )
        }
    }

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