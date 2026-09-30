package me.gblrod.mavira.features.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
    difficulty: (DifficultyOptions),
    cards: List<MemoryCard>,
    onCardClick: (MemoryCard) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isLandscape = maxWidth > maxHeight
        val columns = if (isLandscape) difficulty.landscapeColumns else difficulty.portraitColumns
        val rows = cards.size / columns

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(0.8f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    GameSectionHeader(
                        moveCount = moveCount,
                        pairsFound = pairsFound,
                        pairsTotal = pairsTotal,
                        difficulty = difficulty,
                        difficultyRowModifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 12.dp,
                                horizontal = 16.dp
                            )
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                BoxWithConstraints(
                    modifier = Modifier
                        .weight(1.2f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    val spacing = 2.dp
                    val availableHeight = maxHeight - 2.dp

                    val cardSizeByWidth = (maxWidth - spacing * (columns - 1)) / columns
                    val cardSizeByHeight = (availableHeight - spacing * (rows - 1)) / rows
                    val cardSize = minOf(cardSizeByWidth, cardSizeByHeight)

                    val boardWidth = cardSize * columns + spacing * (columns - 1)
                    val boardHeight = cardSize * rows + spacing * (rows - 1)

                    GameBoard(
                        modifier = Modifier.size(width = boardWidth, height = boardHeight),
                        columns = columns,
                        cards = cards,
                        onCardClick = { onCardClick(it) },
                        spacing = spacing,
                        contentPadding = PaddingValues(0.dp),
                        cardModifier = Modifier.size(cardSize)
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                GameSectionHeader(
                    moveCount = moveCount,
                    pairsFound = pairsFound,
                    pairsTotal = pairsTotal,
                    difficulty = difficulty,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GameBoard(
                            columns = columns,
                            cards = cards,
                            onCardClick = { onCardClick(it) }
                        )
                    }
                }
            }
        }
    }
}