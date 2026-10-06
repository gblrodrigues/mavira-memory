package me.gblrod.mavira.features.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.gblrod.mavira.data.datastore.repository.SettingsRepository
import me.gblrod.mavira.features.game.difficulty.model.DifficultyOptions
import me.gblrod.mavira.features.settings.model.LanguageOptions
import me.gblrod.mavira.features.settings.model.ThemeOptions
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val initialSettings = requireNotNull(
        settingsRepository.settings.value
    )

    val uiState = settingsRepository.settings
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = initialSettings
        )

    fun setTheme(theme: ThemeOptions) {
        viewModelScope.launch {
            settingsRepository.setTheme(theme)
        }
    }

    fun setLanguage(language: LanguageOptions) {
        viewModelScope.launch {
            settingsRepository.setLanguage(language)
        }
    }

    fun setVibration(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setVibration(enabled)
        }
    }

    fun setDifficulty(difficulty: DifficultyOptions) {
        viewModelScope.launch {
            settingsRepository.setDifficulty(difficulty)
        }
    }
}