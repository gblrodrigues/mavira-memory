package me.gblrod.mavira.features.game.engine

import me.gblrod.mavira.features.game.engine.model.GameState
import me.gblrod.mavira.features.game.engine.model.HintResult
import me.gblrod.mavira.features.game.engine.model.PairResult
import me.gblrod.mavira.features.game.engine.model.SelectionResult
import me.gblrod.mavira.features.game.model.MemoryCard
import me.gblrod.mavira.features.game.model.MemorySymbol

class GameEngine {
    var state = GameState()
        private set

    fun startGame(pairs: Int): GameState {
        require(pairs in 1..MemorySymbol.entries.size)

        state = GameState(
            cards = createDeck(pairs = pairs),
            pairsTotal = pairs
        )

        return state
    }

    fun selectCard(cardId: Int): SelectionResult {
        val revealedCards = state.cards.filter {
            it.isRevealed && !it.isMatched
        }

        if (revealedCards.size >= 2) {
            return SelectionResult(
                state = state,
                accepted = false,
                pairReady = false
            )
        }

        val selectedCard = state.cards.firstOrNull {
            it.id == cardId
        }

        if (
            selectedCard == null ||
            selectedCard.isRevealed ||
            selectedCard.isMatched
        ) {
            return SelectionResult(
                state = state,
                accepted = false,
                pairReady = false
            )
        }

        state = state.copy(
            cards = state.cards.map { card ->
                if (card.id == cardId) {
                    card.copy(
                        isRevealed = true
                    )
                } else {
                    card
                }
            },
            moveCount = if (revealedCards.size == 1) {
                state.moveCount + 1
            } else {
                state.moveCount
            }
        )

        return SelectionResult(
            state = state,
            accepted = true,
            pairReady = revealedCards.size == 1
        )
    }

    fun checkPair(): PairResult? {
        val revealedCards = state.cards.filter {
            it.isRevealed && !it.isMatched
        }

        if (revealedCards.size != 2) {
            return null
        }

        val firstCard = revealedCards[0]
        val secondCard = revealedCards[1]

        if (firstCard.symbol == secondCard.symbol) {
            val newPairsFound = state.pairsFound + 1

            state = state.copy(
                cards = state.cards.map { card ->
                    if (
                        card.id == firstCard.id ||
                        card.id == secondCard.id
                    ) {
                        card.copy(
                            isMatched = true
                        )
                    } else {
                        card
                    }
                },
                pairsFound = newPairsFound
            )

            return PairResult(
                state = state,
                firstCardId = firstCard.id,
                secondCardId = secondCard.id,
                isMatch = true,
                isComplete = state.isCompleted
            )
        }

        return PairResult(
            state = state,
            firstCardId = firstCard.id,
            secondCardId = secondCard.id,
            isMatch = false,
            isComplete = false
        )
    }

    fun hidePair(
        firstCardId: Int,
        secondCardId: Int
    ): GameState {
        state = state.copy(
            cards = state.cards.map { card ->
                if (
                    card.id == firstCardId ||
                    card.id == secondCardId
                ) {
                    card.copy(
                        isRevealed = false
                    )
                } else {
                    card
                }
            }
        )

        return state
    }

    fun getHint(): HintResult? {
        val unmatchedCards = state.cards.filter { !it.isMatched }

        val revealedCard = unmatchedCards.firstOrNull { it.isRevealed }

        if (revealedCard != null) {
            val matchingCard = unmatchedCards.firstOrNull {
                it.id != revealedCard.id && it.symbol == revealedCard.symbol
            }

            return matchingCard?.let {
                HintResult(
                    cardIds = listOf(
                        it.id
                    )
                )
            }
        }

        val availablePairs = unmatchedCards
            .groupBy { it.symbol }
            .values
            .filter { it.size == 2 }

        return availablePairs
            .randomOrNull()
            ?.let { pair ->
                HintResult(
                    cardIds = pair.map {
                        it.id
                    }
                )
            }
    }

    fun restart(): GameState {
        return startGame(
            pairs = state.pairsTotal
        )
    }

    private fun createDeck(
        pairs: Int
    ): List<MemoryCard> {
        return MemorySymbol.entries
            .shuffled()
            .take(n = pairs)
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
}
