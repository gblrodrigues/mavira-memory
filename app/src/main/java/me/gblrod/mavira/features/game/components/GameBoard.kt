package me.gblrod.mavira.features.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.gblrod.mavira.features.game.model.MemoryCard

@Composable
fun GameBoard(
    modifier: Modifier = Modifier,
    cards: List<MemoryCard>,
    onCardClick: (MemoryCard) -> Unit,
    columns: Int,
    spacing: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    cardModifier: Modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(count = columns),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing),
        contentPadding = contentPadding,
        userScrollEnabled = false
    ) {
        items(
            items = cards,
            key = { it.id }
        ) { card ->
            MemoryCardItem(
                modifier = cardModifier,
                card = card,
                onClick = {
                    onCardClick(card)
                }
            )
        }
    }
}