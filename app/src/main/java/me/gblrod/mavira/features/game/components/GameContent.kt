package me.gblrod.mavira.features.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.gblrod.mavira.features.game.difficulty.model.DifficultyOptions
import me.gblrod.mavira.features.game.model.MemoryCard

@Composable
fun GameContent(
    moveCount: Int,
    pairsFound: Int,
    pairsTotal: Int,
    isGameStarted: Boolean,
    difficulty: (DifficultyOptions),
    cards: List<MemoryCard>,
    onCardClick: (MemoryCard) -> Unit,
    onRestartClick: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        if (maxWidth > maxHeight) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    GameSectionHeader(
                        moveCount = moveCount,
                        pairsFound = pairsFound,
                        pairsTotal = pairsTotal
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    GameRestartButton(
                        onClick = { onRestartClick() },
                        enabled = isGameStarted
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    GameBoard(
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f),
                        difficulty = difficulty,
                        cards = cards,
                        onCardClick = { onCardClick(it) }
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp)
            ) {
                GameSectionHeader(
                    moveCount = moveCount,
                    pairsFound = pairsFound,
                    pairsTotal = pairsTotal
                )

                Spacer(modifier = Modifier.height(16.dp))

                GameBoard(
                    difficulty = difficulty,
                    cards = cards,
                    onCardClick = { onCardClick(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                GameRestartButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = { onRestartClick() },
                    enabled = isGameStarted
                )
            }
        }
    }
}