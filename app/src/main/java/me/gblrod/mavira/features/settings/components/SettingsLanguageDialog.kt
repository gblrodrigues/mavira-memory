package me.gblrod.mavira.features.settings.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import me.gblrod.mavira.R
import me.gblrod.mavira.features.settings.model.LanguageOptions
import me.gblrod.mavira.shared.components.SingleSelectionDialog

@Composable
fun SettingsLanguageDialog(
    selectedLanguage: LanguageOptions,
    onLanguageSelected: (LanguageOptions) -> Unit,
    onDismiss: () -> Unit
) {
    SingleSelectionDialog(
        title = stringResource(id = R.string.language_dialog_title),
        options = LanguageOptions.entries,
        selectedOption = selectedLanguage,
        optionLabel = { it.label },
        onOptionSelected = { onLanguageSelected(it) },
        onDismiss = { onDismiss() }
    )
}