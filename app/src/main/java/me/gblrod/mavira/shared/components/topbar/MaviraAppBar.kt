package me.gblrod.mavira.shared.components.topbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.gblrod.mavira.R
import me.gblrod.mavira.features.game.config.HintConfig
import me.gblrod.mavira.ui.theme.DarkGreen
import me.gblrod.mavira.ui.theme.LightRed
import me.gblrod.mavira.ui.theme.Yellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaviraAppBar(
    config: TopBarConfig,
    onBackClick: () -> Unit,
    isRestartEnabled: Boolean = false,
    onRestartGame: () -> Unit = {},
    hintsRemaining: Int = HintConfig.INITIAL_HINTS,
    isHintEnabled: Boolean = false,
    onHintGame: () -> Unit = {}
) {
    TopAppBar(
        modifier = Modifier.height(76.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            Text(
                text = stringResource(id = config.title),
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { onBackClick() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            if (config.showHint && config.showRestartGame) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    IconButton(
                        onClick = { onHintGame() },
                        enabled = isHintEnabled
                    ) {
                        BadgedBox(
                            badge = {
                                if (hintsRemaining > 0) {
                                    val badgeColor = when {
                                        hintsRemaining > 2 -> DarkGreen
                                        hintsRemaining > 1 -> Yellow
                                        else -> LightRed
                                    }

                                    Badge(
                                        containerColor = badgeColor
                                    ) {
                                        Text(
                                            text = hintsRemaining.toString(),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = stringResource(id = R.string.button_hint_game)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = { onRestartGame() },
                        enabled = isRestartEnabled
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = stringResource(id = R.string.button_restart_game)
                        )
                    }
                }
            }
        }
    )
}