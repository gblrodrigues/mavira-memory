package me.gblrod.mavira.features.game.challenge.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.gblrod.mavira.R
import me.gblrod.mavira.core.manager.ShareManager
import me.gblrod.mavira.features.game.challenge.viewmodel.ChallengeViewModel
import me.gblrod.mavira.features.game.components.GameContent
import me.gblrod.mavira.features.game.components.GameEffectsHandler
import me.gblrod.mavira.features.game.components.ResultDialog
import me.gblrod.mavira.shared.extension.toTimerFormat

@Composable
fun ChallengeScreen(
    challengeViewModel: ChallengeViewModel,
    onNavigateHome: () -> Unit,
) {
    val uiState by challengeViewModel.uiState.collectAsStateWithLifecycle()
    val isVibrationEnabled by challengeViewModel.isVibrationEnabled.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val shareManager = remember(context) {
        ShareManager(context = context)
    }

    GameEffectsHandler(
        effects = challengeViewModel.effects,
        isVibrationEnabled = isVibrationEnabled
    )

    GameContent(
        moveCount = uiState.moveCount,
        pairsFound = uiState.pairsFound,
        pairsTotal = uiState.difficulty.pairs,
        timer = uiState.timer,
        difficulty = uiState.difficulty,
        cards = uiState.cards,
        onCardClick = {
            challengeViewModel.onCardClick(
                selectedCard = it
            )
        }
    )

    if (uiState.isCompleted) {
        val shareMessage = stringResource(
            id = R.string.share_challenge_complete_result,
            uiState.timer.toTimerFormat(),
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
            onRestartGame = null,
            onShareResult = {
                shareManager.shareText(
                    text = shareMessage
                )
            }
        )
    }

    if (uiState.isFailed) {
        ResultDialog(
            pairsFound = uiState.pairsFound,
            pairsTotal = uiState.difficulty.pairs,
            moveCount = uiState.moveCount,
            playTime = 0,
            onNavigateHome = { onNavigateHome() },
            onRestartGame = null,
            dialogTitle = R.string.dialog_title_failed
        )
    }
}