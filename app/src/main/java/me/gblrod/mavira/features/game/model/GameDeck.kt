package me.gblrod.mavira.features.game.model

import me.gblrod.mavira.features.game.difficulty.model.DifficultyOptions

fun createDeck(
    difficulty: DifficultyOptions
): List<MemoryCard> {
    return MemorySymbol.entries
        .shuffled()
        .take(n = difficulty.pairs)
        .flatMap { symbol ->
            listOf(symbol, symbol)
        }
        .shuffled()
        .mapIndexed { index, symbol ->
            MemoryCard(
                id = index,
                symbol = symbol
            )
        }
}