package me.gblrod.mavira.core.manager

import android.content.Context
import android.content.res.Configuration
import me.gblrod.mavira.features.settings.model.LanguageOptions
import java.util.Locale

object LanguageManager {
    fun resolveLocale(language: LanguageOptions): Locale {
        return when (language) {
            LanguageOptions.PT_BR -> Locale.forLanguageTag("pt-BR")
            LanguageOptions.EN_US -> Locale.forLanguageTag("en")
            LanguageOptions.ES -> Locale.forLanguageTag("es")
        }
    }

    fun applyLocale(base: Context, locale: Locale): Context {
        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        return base.createConfigurationContext(config)
    }

    fun getDeviceLanguage(): LanguageOptions {
        return when (Locale.getDefault().language) {
            "pt" -> LanguageOptions.PT_BR
            "es" -> LanguageOptions.ES
            else -> LanguageOptions.EN_US
        }
    }
}