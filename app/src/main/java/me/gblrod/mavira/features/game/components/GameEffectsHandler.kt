package me.gblrod.mavira.features.game.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.Flow
import me.gblrod.mavira.core.manager.VibrationManager
import me.gblrod.mavira.features.game.model.GameEffect

@Composable
fun GameEffectsHandler(
    effects: Flow<GameEffect>,
    isVibrationEnabled: Boolean
) {
    val context = LocalContext.current
    val vibrationManager = remember(context) {
        VibrationManager(context = context)
    }
    val currentVibrationEnabled by rememberUpdatedState(
        newValue = isVibrationEnabled
    )

    LaunchedEffect(Unit) {
        effects.collect {
            if (!currentVibrationEnabled) return@collect

            when (it) {
                GameEffect.PairMatched -> vibrationManager.pairMatched()
                GameEffect.GameCompleted -> vibrationManager.gameCompleted()
            }
        }
    }
}