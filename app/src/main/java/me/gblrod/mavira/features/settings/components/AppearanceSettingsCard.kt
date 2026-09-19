package me.gblrod.mavira.features.settings.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.gblrod.mavira.R
import me.gblrod.mavira.features.settings.model.LanguageOptions
import me.gblrod.mavira.features.settings.model.SettingsItemData
import me.gblrod.mavira.features.settings.model.ThemeOptions

@Composable
fun AppearanceSettingsCard(
    theme: ThemeOptions,
    language: LanguageOptions,
    onThemeClick: () -> Unit,
    onLanguageClick: () -> Unit
) {
    SettingsCard(
        icon = Icons.Default.Brush,
        title = stringResource(id = R.string.settings_appearance_label),
        items = listOf(
            SettingsItemData(
                title = stringResource(id = R.string.settings_theme_label),
                description = stringResource(id = theme.label),
                icon = theme.icon,
                onClick = { onThemeClick()},
                trailingContent = { SettingsChevron() }
            ),
            SettingsItemData(
                title = stringResource(id = R.string.settings_language_label),
                description = stringResource(id = language.label),
                icon = Icons.Default.Language,
                onClick = { onLanguageClick() },
                trailingContent = { SettingsChevron() }
            )
        )
    )
}

@Composable
private fun SettingsChevron() {
    Icon(
        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
        contentDescription = null,
        modifier = Modifier.size(24.dp),
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}