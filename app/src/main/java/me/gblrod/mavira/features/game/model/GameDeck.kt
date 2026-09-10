package me.gblrod.mavira.features.game.model

fun createDeck(): List<MemoryCard> {
    return MemoryItem.entries
        .flatMap { item ->
            listOf(item, item)
        }
        .shuffled()
        .mapIndexed { index, item ->
            MemoryCard(
                id = index,
                item = item
            )
        }
}