package me.gblrod.mavira.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.core.splashscreen.SplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.gblrod.mavira.core.manager.LanguageManager
import me.gblrod.mavira.data.preferences.SettingsRepository
import me.gblrod.mavira.ui.theme.MaviraTheme

@Composable
fun MaviraRoot(
    settingsRepository: SettingsRepository,
    splashScreen: SplashScreen
) {
    val settings by settingsRepository.settings.collectAsStateWithLifecycle(
        initialValue = null
    )

    splashScreen.setKeepOnScreenCondition {
        settings == null
    }

    settings?.let { settings ->
        val baseContext = LocalContext.current
        val locale = remember(settings.languageOptions) {
            LanguageManager.resolveLocale(language = settings.languageOptions)
        }

        val localizedContext = remember(baseContext, locale) {
            LanguageManager.applyLocale(base = baseContext, locale = locale)
        }

        val configuration = localizedContext.resources.configuration

        MaviraTheme(
            themeOptions = settings.themeOptions
        ) {
            CompositionLocalProvider(
                LocalConfiguration provides configuration,
                LocalResources provides localizedContext.resources
            ) {
                MaviraApp()
            }
        }
    }
}