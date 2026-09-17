package me.gblrod.mavira.features.settings.model

import me.gblrod.mavira.R

enum class LanguageOptions(
    val label: Int
) {
    EN_US(label = R.string.language_english_label),
    PT_BR(label = R.string.language_portuguese_br_label),
    ES(label = R.string.language_espanhol_label)
}