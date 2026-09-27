package me.gblrod.mavira.features.game.engine

import me.gblrod.mavira.features.game.model.MemoryCard
import me.gblrod.mavira.features.game.model.MemorySymbol
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GameEngineTest {
    private lateinit var gameEngine: GameEngine

    @Before
    fun setUp() {
        gameEngine = GameEngine()
    }

    @Test
    fun startGame_easy_Difficulty_createsDeckWith6PairsAnd12Cards() {
        val expectedPairs = 6
        val expectedCards = 12

        val state = gameEngine.startGame(
            pairs = expectedPairs
        )

        assertEquals(expectedPairs, state.pairsTotal)
        assertEquals(expectedCards, state.cards.size)
        assertValidDeckPairs(state.cards, expectedPairs)
    }

    @Test

    fun startGame_medium_Difficulty_createsDeckWith8PairsAnd16Cards() {
        val expectedPairs = 8
        val expectedCards = 16

        val state = gameEngine.startGame(
            pairs = expectedPairs
        )

        assertEquals(expectedPairs, state.pairsTotal)
        assertEquals(expectedCards, state.cards.size)
        assertValidDeckPairs(state.cards, expectedPairs)
    }

    @Test
    fun startGame_hard_Difficulty_createsDeckWith12PairsAnd24Cards() {
        val expectedPairs = 12
        val expectedCards = 24

        val state = gameEngine.startGame(
            pairs = expectedPairs
        )

        assertEquals(expectedPairs, state.pairsTotal)
        assertEquals(expectedCards, state.cards.size)
        assertValidDeckPairs(state.cards, expectedPairs)
    }

    @Test
    fun startGame_expert_Difficulty_createsDeckWith18PairsAnd36Cards() {
        val expectedPairs = 18
        val expectedCards = 36

        val state = gameEngine.startGame(
            pairs = expectedPairs
        )

        assertEquals(expectedPairs, state.pairsTotal)
        assertEquals(expectedCards, state.cards.size)
        assertValidDeckPairs(state.cards, expectedPairs)
    }

    @Test
    fun startGame_initializesGameStateCorrectly() {
        val state = gameEngine.startGame(
            pairs = 6
        )

        assertEquals(0, state.moveCount)
        assertEquals(0, state.pairsFound)

        assertFalse(state.isCompleted)
        assertTrue(
            state.cards.all {
                !it.isRevealed && !it.isMatched
            }
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun startGame_zeroPairs_throwsException() {
        gameEngine.startGame(
            pairs = 0
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun startGame_exceedsMaxPairs_throwsException() {
        gameEngine.startGame(
            pairs = MemorySymbol.entries.size + 1
        )
    }

    @Test
    fun selectCard_firstCard_doesNotIncrementMoveCount() {
        val state = gameEngine.startGame(
            pairs = 2
        )
        val card = state.cards.first()

        val result = gameEngine.selectCard(
            cardId = card.id
        )

        assertEquals(0, result.state.moveCount)
    }

    @Test
    fun selectCard_firstCard_isAcceptedAndRevealed() {
        val state = gameEngine.startGame(
            pairs = 2
        )
        val card = state.cards.first()

        val result = gameEngine.selectCard(
            cardId = card.id
        )

        assertTrue(result.accepted)
        assertFalse(result.pairReady)
        assertTrue(
            result.state.cards.first {
                it.id == card.id
            }.isRevealed
        )
    }

    @Test
    fun selectCard_secondCard_isAccepted() {
        val state = gameEngine.startGame(
            pairs = 2
        )

        val firstCard = state.cards.first()
        val secondCard = state.cards.first {
            it.id != firstCard.id
        }

        gameEngine.selectCard(
            cardId = firstCard.id
        )

        val result = gameEngine.selectCard(
            cardId = secondCard.id
        )

        assertTrue(result.accepted)
    }

    @Test
    fun selectCard_secondCard_setsPairReady() {
        val state = gameEngine.startGame(
            pairs = 2
        )

        val firstCard = state.cards.first()
        val secondCard = state.cards.first {
            it.id != firstCard.id
        }

        gameEngine.selectCard(
            cardId = firstCard.id
        )

        val result = gameEngine.selectCard(
            cardId = secondCard.id
        )

        assertTrue(result.pairReady)
    }

    @Test
    fun selectCard_secondCard_incrementsMoveCount() {
        val state = gameEngine.startGame(
            pairs = 2
        )

        val firstCard = state.cards.first()
        val secondCard = state.cards.first {
            it.id != firstCard.id
        }

        gameEngine.selectCard(
            cardId = firstCard.id
        )

        val result = gameEngine.selectCard(
            cardId = secondCard.id
        )

        assertEquals(1, result.state.moveCount)
    }

    @Test
    fun selectCard_alreadyRevealedCard_isRejected() {
        val state = gameEngine.startGame(
            pairs = 2
        )

        val card = state.cards.first()

        gameEngine.selectCard(
            cardId = card.id
        )

        val result = gameEngine.selectCard(
            cardId = card.id
        )

        assertFalse(result.accepted)
    }

    @Test
    fun selectCard_matchedCard_isRejected() {
        val state = gameEngine.startGame(
            pairs = 1
        )

        val firstCard = state.cards[0]
        val secondCard = state.cards.first {
            it.id != firstCard.id && it.symbol == firstCard.symbol
        }

        gameEngine.selectCard(
            cardId = firstCard.id
        )
        gameEngine.selectCard(
            cardId = secondCard.id
        )
        gameEngine.checkPair()

        val result = gameEngine.selectCard(firstCard.id)

        assertFalse(result.accepted)
    }

    @Test
    fun selectCard_invalidCardId_isRejected() {
        gameEngine.startGame(
            pairs = 2
        )

        val result = gameEngine.selectCard(
            cardId = -1
        )

        assertFalse(result.accepted)
    }

    @Test
    fun selectCard_thirdCard_isRejectedWhenPairIsPending() {
        val state = gameEngine.startGame(
            pairs = 2
        )

        val firstCard = state.cards[0]
        val secondCard = state.cards.first {
            it.id != firstCard.id
        }
        val thirdCard = state.cards.first {
            it.id != firstCard.id && it.id != secondCard.id
        }

        gameEngine.selectCard(
            cardId = firstCard.id
        )
        gameEngine.selectCard(
            cardId = secondCard.id
        )

        val result = gameEngine.selectCard(
            cardId = thirdCard.id
        )

        assertFalse(result.accepted)
    }

    @Test
    fun selectCard_secondCard_keepsBothCardsRevealed() {
        val state = gameEngine.startGame(
            pairs = 2
        )

        val firstCard = state.cards.first()
        val secondCard = state.cards.first {
            it.id != firstCard.id
        }

        gameEngine.selectCard(
            cardId = firstCard.id
        )

        val result = gameEngine.selectCard(
            cardId = secondCard.id
        )

        assertTrue(
            result.state.cards
                .filter { it.id == firstCard.id || it.id == secondCard.id }
                .all { it.isRevealed }
        )
    }

    @Test
    fun checkPair_withoutRevealedPair_returnsNull() {
        gameEngine.startGame(
            pairs = 2
        )

        val result = gameEngine.checkPair()

        assertNull(result)
    }

    @Test
    fun checkPair_matchingCards_returnsMatchTrueAndMarksCardsAsMatched() {
        val state = gameEngine.startGame(
            pairs = 2
        )

        val firstCard = state.cards.first()
        val matchingCard = state.cards.first {
            it.id != firstCard.id && it.symbol == firstCard.symbol
        }

        gameEngine.selectCard(
            cardId = firstCard.id
        )
        gameEngine.selectCard(
            cardId = matchingCard.id
        )

        val result = gameEngine.checkPair()

        assertNotNull(result)
        assertTrue(result!!.isMatch)
        assertEquals(firstCard.id, result.firstCardId)
        assertEquals(matchingCard.id, result.secondCardId)
        assertEquals(1, result.state.pairsFound)

        val updatedCards = result.state.cards.filter {
            it.id == firstCard.id || it.id == matchingCard.id
        }
        assertTrue(updatedCards.all { it.isMatched })
    }

    @Test
    fun checkPair_nonMatchingCards_returnsMatchFalse() {
        val state = gameEngine.startGame(
            pairs = 2
        )

        val firstCard = state.cards.first()
        val nonMatchingCard = state.cards.first {
            it.symbol != firstCard.symbol
        }

        gameEngine.selectCard(
            cardId = firstCard.id
        )
        gameEngine.selectCard(
            cardId = nonMatchingCard.id
        )

        val result = gameEngine.checkPair()

        assertNotNull(result)
        assertFalse(result!!.isMatch)
        assertEquals(0, result.state.pairsFound)
        assertFalse(result.isComplete)
    }

    @Test
    fun checkPair_lastPairMatched_setsIsCompleteToTrue() {
        val state = gameEngine.startGame(
            pairs = 1
        )

        val firstCard = state.cards[0]
        val secondCard = state.cards[1]

        gameEngine.selectCard(
            cardId = firstCard.id
        )
        gameEngine.selectCard(
            cardId = secondCard.id
        )

        val result = gameEngine.checkPair()

        assertNotNull(result)
        assertTrue(result!!.isMatch)
        assertTrue(result.isComplete)
        assertTrue(result.state.isCompleted)
    }

    @Test
    fun checkPair_withOnlyOneRevealedCard_returnsNull() {
        val state = gameEngine.startGame(
            pairs = 2
        )

        val card = state.cards.first()

        gameEngine.selectCard(
            cardId = card.id
        )

        val result = gameEngine.checkPair()

        assertNull(result)
    }

    @Test
    fun hidePair_hidesSpecifiedCards() {
        val state = gameEngine.startGame(
            pairs = 2
        )

        val firstCard = state.cards.first()
        val secondCard = state.cards.first {
            it.symbol != firstCard.symbol
        }

        gameEngine.selectCard(
            cardId = firstCard.id
        )
        gameEngine.selectCard(
            cardId = secondCard.id
        )

        val newState = gameEngine.hidePair(
            firstCardId = firstCard.id,
            secondCardId = secondCard.id
        )

        val hiddenCards = newState.cards.filter {
            it.id == firstCard.id || it.id == secondCard.id
        }
        assertTrue(hiddenCards.all { !it.isRevealed })
    }

    @Test
    fun restart_resetsGameStateAndKeepsPairsTotal() {
        val initialState = gameEngine.startGame(
            pairs = 4
        )

        val firstCard = initialState.cards.first()
        val secondCard = initialState.cards.first {
            it.id != firstCard.id
        }

        gameEngine.selectCard(
            cardId = firstCard.id
        )
        gameEngine.selectCard(
            cardId = secondCard.id
        )

        val restartedState = gameEngine.restart()

        assertEquals(4, restartedState.pairsTotal)
        assertEquals(8, restartedState.cards.size)
        assertEquals(0, restartedState.moveCount)
        assertEquals(0, restartedState.pairsFound)
        assertFalse(restartedState.isCompleted)
        assertTrue(restartedState.cards.all { !it.isRevealed && !it.isMatched })
    }

    private fun assertValidDeckPairs(cards: List<MemoryCard>, expectedPairs: Int) {
        val symbolCounts = cards.groupBy { it.symbol }
        assertEquals(expectedPairs, symbolCounts.keys.size)
        assertTrue(symbolCounts.values.all { it.size == 2 })

        assertEquals(
            cards.size,
            cards.map {
                it.id
            }.distinct().size
        )
    }
}