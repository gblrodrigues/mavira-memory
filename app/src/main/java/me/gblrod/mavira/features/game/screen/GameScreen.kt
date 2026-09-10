package me.gblrod.mavira.features.game.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.gblrod.mavira.features.game.components.GameBoard
import me.gblrod.mavira.features.game.components.GameSectionHeader
import me.gblrod.mavira.features.game.components.ResultDialog
import me.gblrod.mavira.features.game.viewmodel.GameViewModel

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel(),
    onNavigateHome: () -> Unit
) {
    val uiState by gameViewModel.uiState.collectAsStateWithLifecycle()

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
            onCardClick = gameViewModel::onCardClick
        )
    }

    if (uiState.pairsFound == uiState.pairsTotal) {
        ResultDialog(
            pairFound = uiState.pairsFound,
            pairTotal = uiState.pairsTotal,
            onNavigateHome = { onNavigateHome() },
            onRestartGame = { gameViewModel.restart() }
        )
    }
}