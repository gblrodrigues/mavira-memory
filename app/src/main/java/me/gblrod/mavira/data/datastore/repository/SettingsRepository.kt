package me.gblrod.mavira.data.datastore.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.gblrod.mavira.core.manager.LanguageManager.getDeviceLanguage
import me.gblrod.mavira.data.datastore.maviraDataStore
import me.gblrod.mavira.features.game.difficulty.model.DifficultyOptions
import me.gblrod.mavira.features.settings.model.LanguageOptions
import me.gblrod.mavira.features.settings.model.ThemeOptions
import me.gblrod.mavira.features.settings.state.SettingsUiState
import javax.inject.Inject
import javax.inject.Singleton

val themeKey = stringPreferencesKey(name = "theme")
val languageKey = stringPreferencesKey(name = "language")
val vibrationKey = booleanPreferencesKey(name = "vibration")
val difficultyKey = stringPreferencesKey(name = "difficulty")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val repositoryScope = CoroutineScope(
        context = SupervisorJob() + Dispatchers.IO
    )

    val settings: StateFlow<SettingsUiState?> =
        context.maviraDataStore.data.map { prefs ->
            SettingsUiState(
                themeOptions = prefs[themeKey]
                    ?.let(block = ThemeOptions::valueOf)
                    ?: ThemeOptions.SYSTEM,

                languageOptions = prefs[languageKey]
                    ?.let(block = LanguageOptions::valueOf)
                    ?: getDeviceLanguage(),

                vibrationEnabled = prefs[vibrationKey] ?: true,

                difficultyOptions = prefs[difficultyKey]
                    ?.let(block = DifficultyOptions::valueOf)
                    ?: DifficultyOptions.EASY
            )
        }.stateIn(
                scope = repositoryScope,
                started = SharingStarted.Eagerly,
                initialValue = null
            )

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

    suspend fun setDifficulty(difficulty: DifficultyOptions) {
        context.maviraDataStore.edit { prefs ->
            prefs[difficultyKey] = difficulty.name
        }
    }
}