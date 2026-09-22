package me.gblrod.mavira.features.settings.state

import me.gblrod.mavira.features.game.difficulty.model.DifficultyOptions
import me.gblrod.mavira.features.settings.model.LanguageOptions
import me.gblrod.mavira.features.settings.model.ThemeOptions

data class SettingsUiState(
    val themeOptions: ThemeOptions = ThemeOptions.SYSTEM,
    val languageOptions: LanguageOptions = LanguageOptions.EN_US,
    val vibrationEnabled: Boolean = true,
    val difficultyOptions: DifficultyOptions = DifficultyOptions.EASY
)