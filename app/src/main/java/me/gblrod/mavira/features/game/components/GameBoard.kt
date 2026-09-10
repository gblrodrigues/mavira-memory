package me.gblrod.mavira.features.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import me.gblrod.mavira.features.game.model.MemoryCard

@Composable
fun GameBoard(
    cards: List<MemoryCard>,
    onCardClick: (MemoryCard) -> Unit,
    gridSize: Int
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = gridSize),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(
            items = cards,
            key = { it.id }
        ) { card ->
            MemoryCardItem(
                card = card,
                onClick = {
                    onCardClick(card)
                }
            )
        }
    }
}