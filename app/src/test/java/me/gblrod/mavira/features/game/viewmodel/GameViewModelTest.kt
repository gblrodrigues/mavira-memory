package me.gblrod.mavira.features.game.viewmodel

import android.os.SystemClock
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkStatic
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import me.gblrod.mavira.data.preferences.SettingsRepository
import me.gblrod.mavira.data.room.repository.GameRepository
import me.gblrod.mavira.features.game.difficulty.model.DifficultyOptions
import me.gblrod.mavira.features.game.model.GameEffect
import me.gblrod.mavira.features.game.model.GameResult
import me.gblrod.mavira.features.settings.model.LanguageOptions
import me.gblrod.mavira.features.settings.model.ThemeOptions
import me.gblrod.mavira.features.settings.state.SettingsUiState
import me.gblrod.mavira.rules.MainDispatcherRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var gameRepository: GameRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var gameViewModel: GameViewModel
    private var currentTime = 1_000L

    @Before
    fun setUp() {
        mockkStatic(SystemClock::class)

        every {
            SystemClock.elapsedRealtime()
        } answers {
            currentTime
        }

        gameRepository = mockk(relaxed = true)
        settingsRepository = mockk()
    }

    @After
    fun tearDown() {
        unmockkStatic(SystemClock::class)
    }

    @Test
    fun initialState_withEasyDifficulty_createsCorrectDeck() {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        val state = gameViewModel.uiState.value

        assertEquals(DifficultyOptions.EASY, state.difficulty)
        assertEquals(DifficultyOptions.EASY.pairs * 2, state.cards.size)
    }

    @Test
    fun initialState_withMediumDifficulty_createsCorrectDeck() {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.MEDIUM
        )

        val state = gameViewModel.uiState.value

        assertEquals(DifficultyOptions.MEDIUM, state.difficulty)
        assertEquals(DifficultyOptions.MEDIUM.pairs * 2, state.cards.size)
    }

    @Test
    fun initialState_withHardDifficulty_createsCorrectDeck() {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.HARD
        )

        val state = gameViewModel.uiState.value

        assertEquals(DifficultyOptions.HARD, state.difficulty)
        assertEquals(DifficultyOptions.HARD.pairs * 2, state.cards.size)
    }

    @Test
    fun initialState_withExpertDifficulty_createsCorrectDeck() {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EXPERT
        )

        val state = gameViewModel.uiState.value

        assertEquals(DifficultyOptions.EXPERT, state.difficulty)
        assertEquals(DifficultyOptions.EXPERT.pairs * 2, state.cards.size)
    }

    @Test
    fun initialState_startsWithZeroMoves() {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        val state = gameViewModel.uiState.value

        assertEquals(0, state.moveCount)
    }

    @Test
    fun initialState_startsWithZeroPairsFound() {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        val state = gameViewModel.uiState.value

        assertEquals(0, state.pairsFound)
    }

    @Test
    fun initialState_startsWithZeroTimer() {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        val state = gameViewModel.uiState.value

        assertEquals(0, state.timer)
    }

    @Test
    fun initialState_isGameNotStarted() {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        val state = gameViewModel.uiState.value

        assertFalse(state.isGameStarted)
    }

    @Test
    fun initialState_vibrationIsEnabled() {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        assertTrue(gameViewModel.isVibrationEnabled.value)
    }

    @Test
    fun isVibrationEnabled_reactsToSettingsChanges() = runTest {
        val settingsFlow = MutableStateFlow(
            SettingsUiState(
                themeOptions = ThemeOptions.SYSTEM,
                languageOptions = LanguageOptions.PT_BR,
                vibrationEnabled = true,
                difficultyOptions = DifficultyOptions.EASY
            )
        )
        every { settingsRepository.settings } returns settingsFlow

        gameViewModel = GameViewModel(
            gameRepository = gameRepository,
            settingsRepository = settingsRepository
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            gameViewModel.isVibrationEnabled.collect {}
        }

        assertTrue(gameViewModel.isVibrationEnabled.value)

        settingsFlow.value = settingsFlow.value.copy(vibrationEnabled = false)
        runCurrent()

        assertFalse(gameViewModel.isVibrationEnabled.value)
    }

    @Test
    fun onCardClick_firstCard_startsGame() {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        val firstCard = gameViewModel.uiState.value.cards.first()

        gameViewModel.onCardClick(
            selectedCard = firstCard
        )

        assertTrue(gameViewModel.uiState.value.isGameStarted)
    }

    @Test
    fun onCardClick_firstCard_doesNotIncrementMoveCount() {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        val state = gameViewModel.uiState.value

        val firstCard = state.cards.first()

        gameViewModel.onCardClick(
            selectedCard = firstCard
        )

        val newState = gameViewModel.uiState.value

        assertEquals(0, newState.moveCount)
    }

    @Test
    fun onCardClick_firstCard_isRevealed() {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        val state = gameViewModel.uiState.value

        val firstCard = state.cards.first()

        gameViewModel.onCardClick(
            selectedCard = firstCard
        )

        val newState = gameViewModel.uiState.value

        assertTrue(newState.cards.first {
            it.id == firstCard.id
        }.isRevealed)
    }

    @Test
    fun onCardClick_secondCard_incrementsMoveCount() {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        val state = gameViewModel.uiState.value

        val firstCard = state.cards.first()
        val secondCard = state.cards.first {
            it.id != firstCard.id
        }

        gameViewModel.onCardClick(
            selectedCard = firstCard
        )

        gameViewModel.onCardClick(
            selectedCard = secondCard
        )

        val newState = gameViewModel.uiState.value

        assertEquals(1, newState.moveCount)
    }

    @Test
    fun onCardClick_secondCard_matchingPair_updatesPairsFound() = runTest {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        val state = gameViewModel.uiState.value

        val firstCard = state.cards.first()
        val secondCard = state.cards.first {
            it.id != firstCard.id && it.symbol == firstCard.symbol
        }

        gameViewModel.onCardClick(
            selectedCard = firstCard
        )

        gameViewModel.onCardClick(
            selectedCard = secondCard
        )

        runCurrent()
        advanceTimeBy(delayTime = 1_000.milliseconds)

        val newState = gameViewModel.uiState.value

        assertEquals(1, newState.pairsFound)

        gameViewModel.restart()
    }

    @Test
    fun onCardClick_secondCard_matchingPair_emitsPairMatched() = runTest {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        val state = gameViewModel.uiState.value

        val firstCard = state.cards.first()
        val secondCard = state.cards.first {
            it.id != firstCard.id && it.symbol == firstCard.symbol
        }

        var emittedEffect: GameEffect? = null
        backgroundScope.launch(
            context = UnconfinedTestDispatcher(scheduler = testScheduler)
        ) {
            gameViewModel.effects.collect { effect ->
                emittedEffect = effect
            }
        }

        gameViewModel.onCardClick(
            selectedCard = firstCard
        )

        gameViewModel.onCardClick(
            selectedCard = secondCard
        )

        assertEquals(GameEffect.PairMatched, emittedEffect)

        gameViewModel.restart()
    }

    @Test
    fun onCardClick_secondCard_nonMatchingPair_hidesCardsAfterDelay() = runTest {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        val state = gameViewModel.uiState.value

        val firstCard = state.cards.first()
        val nonMatchingCard = state.cards.first {
            it.id != firstCard.id && it.symbol != firstCard.symbol
        }

        gameViewModel.onCardClick(
            selectedCard = firstCard
        )

        gameViewModel.onCardClick(
            selectedCard = nonMatchingCard
        )

        assertTrue(gameViewModel.uiState.value.isCheckingPair)

        advanceTimeBy(delayTime = 1_000.milliseconds)
        runCurrent()

        val newState = gameViewModel.uiState.value

        assertFalse(newState.isCheckingPair)

        val hiddenCards = newState.cards.filter {
            it.id == firstCard.id || it.id == nonMatchingCard.id
        }

        assertTrue(hiddenCards.all { !it.isRevealed })

        gameViewModel.restart()
    }

    @Test
    fun onCardClick_whenCheckingPair_ignoresNewCardClicks() = runTest {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        val state = gameViewModel.uiState.value

        val firstCard = state.cards.first()
        val nonMatchingCard = state.cards.first {
            it.id != firstCard.id && it.symbol != firstCard.symbol
        }
        val thirdCard = state.cards.first {
            it.id != firstCard.id && it.id != nonMatchingCard.id
        }

        gameViewModel.onCardClick(
            selectedCard = firstCard
        )
        gameViewModel.onCardClick(
            selectedCard = nonMatchingCard
        )

        assertTrue(gameViewModel.uiState.value.isCheckingPair)

        gameViewModel.onCardClick(
            selectedCard = thirdCard
        )

        val thirdCardState = gameViewModel.uiState.value.cards.first {
            it.id == thirdCard.id
        }

        assertFalse(thirdCardState.isRevealed)

        advanceTimeBy(delayTime = 1_000.milliseconds)
        runCurrent()

        gameViewModel.restart()
    }

    @Test
    fun onCardClick_lastPair_completesGameEmitsEffectAndSavesResult() = runTest {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        val state = gameViewModel.uiState.value
        val distinctSymbols = state.cards.map { it.symbol }.distinct()
        val resultSlot = slot<GameResult>()

        for (i in 0 until distinctSymbols.size - 1) {
            val symbol = distinctSymbols[i]
            val pairCards = state.cards.filter { it.symbol == symbol }

            gameViewModel.onCardClick(
                selectedCard = pairCards[0]
            )

            gameViewModel.onCardClick(
                selectedCard = pairCards[1]
            )

            runCurrent()
        }

        var emittedEffect: GameEffect? = null
        backgroundScope.launch(
            context = UnconfinedTestDispatcher(scheduler = testScheduler)
        ) {
            gameViewModel.effects.collect { effect ->
                emittedEffect = effect
            }
        }

        val lastSymbol = distinctSymbols.last()
        val lastPairCards = state.cards.filter { it.symbol == lastSymbol }

        gameViewModel.onCardClick(
            selectedCard = lastPairCards[0]
        )

        gameViewModel.onCardClick(
            selectedCard = lastPairCards[1]
        )

        runCurrent()

        assertEquals(GameEffect.GameCompleted, emittedEffect)

        coVerify(exactly = 1) {
            gameRepository.saveResult(capture(resultSlot))
        }

        gameViewModel.restart()

        assertEquals(DifficultyOptions.EASY, resultSlot.captured.difficulty)
        assertEquals(DifficultyOptions.EASY.pairs, resultSlot.captured.moves)
    }

    @Test
    fun startTimer_updatesTimerValueOverTime() = runTest {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        val firstCard = gameViewModel.uiState.value.cards.first()

        gameViewModel.onCardClick(
            selectedCard = firstCard
        )

        currentTime = 4_000L
        advanceTimeBy(delayTime = 3_000.milliseconds)
        runCurrent()

        assertEquals(3L, gameViewModel.uiState.value.timer)

        gameViewModel.restart()
    }

    @Test
    fun restart_resetsGameToInitialState() {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        val state = gameViewModel.uiState.value

        val firstCard = state.cards.first()
        val secondCard = state.cards.first {
            it.id != firstCard.id
        }

        gameViewModel.onCardClick(
            selectedCard = firstCard
        )
        gameViewModel.onCardClick(
            selectedCard = secondCard
        )

        currentTime = 20_000L

        gameViewModel.restart()

        val newState = gameViewModel.uiState.value

        assertEquals(0, newState.moveCount)
        assertEquals(0, newState.pairsFound)
        assertEquals(0, newState.timer)
        assertEquals(DifficultyOptions.EASY, newState.difficulty)
        assertEquals(DifficultyOptions.EASY.pairs * 2, newState.cards.size)
        assertFalse(newState.isGameStarted)
        assertFalse(newState.isCheckingPair)
    }

    @Test
    fun onHintClick_noCardRevealed_decrementsHintsRemainingAndGlowsAvailablePair() = runTest {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        val initialState = gameViewModel.uiState.value
        assertEquals(3, initialState.hintsRemaining)

        val firstCard = initialState.cards.first()
        val secondCard = initialState.cards.first {
            it.id != firstCard.id && it.symbol != firstCard.symbol
        }

        gameViewModel.onCardClick(
            selectedCard = firstCard
        )
        gameViewModel.onCardClick(
            selectedCard = secondCard
        )

        advanceTimeBy(delayTime = 1_000.milliseconds)
        runCurrent()

        gameViewModel.onHintClick()

        val hintedState = gameViewModel.uiState.value
        assertEquals(2, hintedState.hintsRemaining)

        val glowingCards = hintedState.cards.filter { it.isGlowing }
        assertEquals(2, glowingCards.size)
        assertEquals(glowingCards[0].symbol, glowingCards[1].symbol)

        advanceTimeBy(delayTime = 3_000.milliseconds)
        runCurrent()

        val afterGlowState = gameViewModel.uiState.value
        assertTrue(afterGlowState.cards.all { !it.isGlowing })

        gameViewModel.restart()
    }

    @Test
    fun onHintClick_oneCardRevealed_glowingMatchingPartnerCard() = runTest {
        gameViewModel = createGameViewModel(
            difficulty = DifficultyOptions.EASY
        )

        val firstCard = gameViewModel.uiState.value.cards.first()
        gameViewModel.onCardClick(selectedCard = firstCard)

        gameViewModel.onHintClick()

        val hintedState = gameViewModel.uiState.value
        assertEquals(2, hintedState.hintsRemaining)

        val glowingCards = hintedState.cards.filter { it.isGlowing }
        assertEquals(1, glowingCards.size)
        assertEquals(firstCard.symbol, glowingCards.first().symbol)

        advanceTimeBy(delayTime = 3_000.milliseconds)
        runCurrent()

        val afterGlowState = gameViewModel.uiState.value
        assertTrue(afterGlowState.cards.all { !it.isGlowing })

        gameViewModel.restart()
    }

    private fun createGameViewModel(
        difficulty: DifficultyOptions
    ): GameViewModel {
        every {
            settingsRepository.settings
        } returns MutableStateFlow(
            SettingsUiState(
                themeOptions = ThemeOptions.SYSTEM,
                languageOptions = LanguageOptions.PT_BR,
                vibrationEnabled = true,
                difficultyOptions = difficulty
            )
        )

        return GameViewModel(
            gameRepository = gameRepository,
            settingsRepository = settingsRepository
        )
    }
}