package me.gblrod.mavira.features.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.gblrod.mavira.R
import me.gblrod.mavira.shared.components.SelectionAlertDialog
import me.gblrod.mavira.shared.extension.toTimerFormat

@Composable
fun GameRestartDialog(
    onRestartGame: () -> Unit,
    onDismiss: () -> Unit,
    playTime: Long,
    moveCount: Int,
    pairsFound: Int,
    pairsTotal: Int
) {
    SelectionAlertDialog(
        title = {
            Text(
                text = stringResource(id = R.string.dialog_restart_game_title)
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.dialog_restart_game_message),
                    color = MaterialTheme.colorScheme.onSurface
                )

                HorizontalDivider(thickness = 1.dp)

                GameProgressRow(
                    label = stringResource(id = R.string.dialog_restart_game_pairs),
                    value = "$pairsFound / $pairsTotal"
                )

                GameProgressRow(
                    label = stringResource(id = R.string.dialog_restart_game_moves),
                    value = "$moveCount"
                )

                GameProgressRow(
                    label = stringResource(id = R.string.dialog_restart_game_play_time),
                    value = playTime.toTimerFormat()
                )
            }
        },
        onConfirm = { onRestartGame() },
        confirmText = stringResource(id = R.string.dialog_restart_game_confirm),
        onDismiss = { onDismiss() },
        dismissText = stringResource(id = R.string.dialog_restart_game_cancel)
    )
}