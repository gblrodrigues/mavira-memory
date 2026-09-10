package me.gblrod.mavira.features.game.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import me.gblrod.mavira.R
import me.gblrod.mavira.shared.components.SelectionAlertDialog

@Composable
fun ResultDialog(
    pairFound: Int,
    pairTotal: Int,
    onNavigateHome: () -> Unit,
    onRestartGame: () -> Unit
) {
    SelectionAlertDialog(
        title = {
            Text(
                text = stringResource(id = R.string.dialog_title)
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.dialog_message, pairFound, pairTotal)
            )
        },
        onConfirm = { onRestartGame() },
        confirmText = stringResource(id = R.string.dialog_play_again),
        onDismiss = { onNavigateHome() },
        dismissText = stringResource(id = R.string.dialog_home)
    )
}