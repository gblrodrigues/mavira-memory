package me.gblrod.mavira.features.game.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.gblrod.mavira.R
import me.gblrod.mavira.ui.theme.ButtonContainerDialog

@Composable
fun GameRestartButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = { onClick() },
        enabled = enabled,
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ButtonContainerDialog),
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) Color.Transparent else ButtonContainerDialog
        ),
        modifier = modifier.fillMaxWidth(0.5f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.RestartAlt,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(id = R.string.button_restart_game),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}