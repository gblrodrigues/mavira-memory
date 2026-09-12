package me.gblrod.mavira.features.game.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.gblrod.mavira.features.game.components.GameBoard
import me.gblrod.mavira.features.game.components.GameRestartButton
import me.gblrod.mavira.features.game.components.GameRestartDialog
import me.gblrod.mavira.features.game.components.GameSectionHeader
import me.gblrod.mavira.features.game.components.ResultDialog
import me.gblrod.mavira.features.game.viewmodel.GameViewModel

@Composable
fun GameScreen(
    gameViewModel: GameViewModel,
    onNavigateHome: () -> Unit
) {
    val uiState by gameViewModel.uiState.collectAsStateWithLifecycle()

    val showButtonRestart = uiState.isGameStarted
    var isRestartGameDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        GameSectionHeader(
            moveCount = uiState.moveCount,
            pairsFound = uiState.pairsFound,
            pairsTotal = uiState.pairsTotal
        )

        Spacer(modifier = Modifier.height(16.dp))

        GameBoard(
            gridSize = uiState.gridSize,
            cards = uiState.cards,
            onCardClick = { gameViewModel.onCardClick(selectedCard = it) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        GameRestartButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { isRestartGameDialog = true },
            enabled = showButtonRestart
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