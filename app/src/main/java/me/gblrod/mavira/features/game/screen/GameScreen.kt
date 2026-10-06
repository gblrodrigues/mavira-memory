package me.gblrod.mavira.features.game.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.gblrod.mavira.R
import me.gblrod.mavira.core.manager.ShareManager
import me.gblrod.mavira.features.game.components.GameContent
import me.gblrod.mavira.features.game.components.GameEffectsHandler
import me.gblrod.mavira.features.game.components.GameRestartDialog
import me.gblrod.mavira.features.game.components.ResultDialog
import me.gblrod.mavira.features.game.viewmodel.GameViewModel
import me.gblrod.mavira.shared.components.topbar.TopBarState
import me.gblrod.mavira.shared.extension.toTimerFormat

@Composable
fun GameScreen(
    gameViewModel: GameViewModel,
    onNavigateHome: () -> Unit,
    onTopBarStateChange: (TopBarState) -> Unit
) {
    val uiState by gameViewModel.uiState.collectAsStateWithLifecycle()
    val isVibrationEnabled by gameViewModel.isVibrationEnabled.collectAsStateWithLifecycle()

    var isRestartGameDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val shareManager = remember(context) {
        ShareManager(context = context)
    }

    val isGameStarted = uiState.isGameStarted
    val hintsRemaining = uiState.hintsRemaining
    val isCheckingPair = uiState.isCheckingPair
    val isHintShowing = uiState.isHintShowing

    LaunchedEffect(
        uiState.timer,
        isGameStarted,
        hintsRemaining,
        isCheckingPair,
        isHintShowing
    ) {
        onTopBarStateChange(
            TopBarState(
                isRestartEnabled = isGameStarted,
                onRestartGame = { isRestartGameDialog = true },
                hintsRemaining = hintsRemaining,
                isHintEnabled = isGameStarted &&
                        hintsRemaining > 0 &&
                        !isCheckingPair &&
                        !isHintShowing &&
                        uiState.pairsFound < uiState.difficulty.pairs,
                onHintGame = { gameViewModel.onHintClick() }
            )
        )
    }

    GameEffectsHandler(
        effects = gameViewModel.effects,
        isVibrationEnabled = isVibrationEnabled
    )

    GameContent(
        moveCount = uiState.moveCount,
        pairsFound = uiState.pairsFound,
        pairsTotal = uiState.difficulty.pairs,
        timer = uiState.timer,
        difficulty = uiState.difficulty,
        cards = uiState.cards,
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
            pairsTotal = uiState.difficulty.pairs
        )
    }

    if (uiState.pairsFound == uiState.difficulty.pairs) {
        val difficultyLabel = stringResource(id = uiState.difficulty.label)

        val shareMessage = stringResource(
            id = R.string.share_game_result,
            uiState.timer.toTimerFormat(),
            difficultyLabel,
            uiState.moveCount,
            uiState.pairsFound,
            uiState.difficulty.pairs
        )

        ResultDialog(
            pairsFound = uiState.pairsFound,
            pairsTotal = uiState.difficulty.pairs,
            moveCount = uiState.moveCount,
            playTime = uiState.timer,
            onNavigateHome = { onNavigateHome() },
            onRestartGame = { gameViewModel.restart() },
            onShareResult = {
                shareManager.shareText(
                    text = shareMessage
                )
            }
        )
    }
}