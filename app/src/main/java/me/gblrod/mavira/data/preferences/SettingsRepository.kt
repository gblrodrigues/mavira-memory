package me.gblrod.mavira.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.gblrod.mavira.core.manager.LanguageManager.getDeviceLanguage
import me.gblrod.mavira.features.settings.model.LanguageOptions
import me.gblrod.mavira.features.settings.model.ThemeOptions
import me.gblrod.mavira.features.settings.state.SettingsUiState
import javax.inject.Inject

val themeKey = stringPreferencesKey(name = "theme")
val languageKey = stringPreferencesKey(name = "language")
val vibrationKey = booleanPreferencesKey(name = "vibration")

class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val settings: Flow<SettingsUiState> =
        context.maviraDataStore.data.map { prefs ->
            SettingsUiState(
                themeOptions = prefs[themeKey]
                    ?.let(block = ThemeOptions::valueOf)
                    ?: ThemeOptions.SYSTEM,

                languageOptions = prefs[languageKey]
                    ?.let(block = LanguageOptions::valueOf)
                    ?: getDeviceLanguage(),

                vibrationEnabled = prefs[vibrationKey] ?: true
            )
        }

    suspend fun setTheme(theme: ThemeOptions) {
        context.maviraDataStore.edit { prefs ->
            prefs[themeKey] = theme.name
        }
    }

    suspend fun setLanguage(language: LanguageOptions) {
        context.maviraDataStore.edit { prefs ->
            prefs[languageKey] = language.name
        }
    }

    suspend fun setVibration(enabled: Boolean) {
        context.maviraDataStore.edit { prefs ->
            prefs[vibrationKey] = enabled
        }
    }
}