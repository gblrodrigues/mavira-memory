package me.gblrod.mavira.features.settings.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import me.gblrod.mavira.R
import me.gblrod.mavira.features.settings.model.ThemeOptions
import me.gblrod.mavira.shared.components.SingleSelectionDialog

@Composable
fun SettingsThemeDialog(
    selectedTheme: ThemeOptions,
    onThemeSelected: (ThemeOptions) -> Unit,
    onDismiss: () -> Unit
) {
    SingleSelectionDialog(
        title = stringResource(id = R.string.theme_dialog_title),
        options = ThemeOptions.entries,
        selectedOption = selectedTheme,
        optionLabel = { it.label },
        onOptionSelected = { onThemeSelected(it) },
        onDismiss = { onDismiss() }
    )
}