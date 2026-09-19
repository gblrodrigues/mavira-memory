package me.gblrod.mavira.features.game.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.gblrod.mavira.features.game.components.GameContent
import me.gblrod.mavira.features.game.components.GameEffectsHandler
import me.gblrod.mavira.features.game.components.GameRestartDialog
import me.gblrod.mavira.features.game.components.ResultDialog
import me.gblrod.mavira.features.game.viewmodel.GameViewModel

@Composable
fun GameScreen(
    gameViewModel: GameViewModel,
    onNavigateHome: () -> Unit
) {
    val uiState by gameViewModel.uiState.collectAsStateWithLifecycle()
    val isVibrationEnabled by gameViewModel.isVibrationEnabled.collectAsStateWithLifecycle()

    var isRestartGameDialog by remember { mutableStateOf(false) }

    GameEffectsHandler(
        effects = gameViewModel.effects,
        isVibrationEnabled = isVibrationEnabled
    )

    GameContent(
        moveCount = uiState.moveCount,
        pairsFound = uiState.pairsFound,
        pairsTotal = uiState.pairsTotal,
        isGameStarted = uiState.isGameStarted,
        gridSize = uiState.gridSize,
        cards = uiState.cards,
        onRestartClick = { isRestartGameDialog = true },
        onCardClick = { gameViewModel.onCardClick(selectedCard = it) }
    )

    if (isRestartGameDialog) {
        GameRestartDialog(
            onRestartGame = {
                gameViewModel.restart()
                isRestartGameDialog = false
            },
            onDismiss = { isRestartGameDialog = false },
            playTime = uiState.timer,
            moveCount = uiState.moveCount,
            pairsFound = uiState.pairsFound,
            pairsTotal = uiState.pairsTotal
        )
    }

    if (uiState.pairsFound == uiState.pairsTotal) {
        ResultDialog(
            pairFound = uiState.pairsFound,
            pairTotal = uiState.pairsTotal,
            playTime = uiState.timer,
            onNavigateHome = { onNavigateHome() },
            onRestartGame = { gameViewModel.restart() }
        )
    }
}