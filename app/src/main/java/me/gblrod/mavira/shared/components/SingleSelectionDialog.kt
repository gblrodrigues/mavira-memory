package me.gblrod.mavira.shared.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import me.gblrod.mavira.R
import me.gblrod.mavira.ui.theme.LightBlue

@Composable
fun <T> SingleSelectionDialog(
    title: String,
    options: List<T>,
    selectedOption: T,
    optionLabel: (T) -> Int,
    onOptionSelected: (T) -> Unit,
    onDismiss: () -> Unit
) {
    var currentSelection by remember(selectedOption) { mutableStateOf(selectedOption) }

    SelectionAlertDialog(
        title = {
            Text(
                text = title
            )
        },
        text = {
            Column {
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { currentSelection = option }
                    ) {
                        RadioButton(
                            selected = currentSelection == option,
                            onClick = { currentSelection = option },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = LightBlue
                            )
                        )
                        Text(
                            text = stringResource(id = optionLabel(option)),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        onDismiss = { onDismiss() },
        onConfirm = {
            onOptionSelected(currentSelection)
            onDismiss()
        },
        confirmText = stringResource(id = R.string.dialog_action_confirm),
        dismissText = stringResource(id = R.string.dialog_action_dismiss)
    )
}