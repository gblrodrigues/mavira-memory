package me.gblrod.mavira.features.game.challenge.viewmodel

import android.os.SystemClock
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import me.gblrod.mavira.data.datastore.repository.ChallengeRepository
import me.gblrod.mavira.data.datastore.repository.SettingsRepository
import me.gblrod.mavira.features.game.difficulty.model.DifficultyOptions
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
class ChallengeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var challengeRepository: ChallengeRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var viewModel: ChallengeViewModel

    private val settingsFlow = MutableStateFlow<SettingsUiState?>(
        SettingsUiState(
            themeOptions = ThemeOptions.SYSTEM,
            languageOptions = LanguageOptions.PT_BR,
            vibrationEnabled = true,
            difficultyOptions = DifficultyOptions.MEDIUM
        )
    )

    private var currentTime = 1_000L

    @Before
    fun setUp() {
        mockkStatic(SystemClock::class)

        every {
            SystemClock.elapsedRealtime()
        } answers {
            currentTime
        }

        challengeRepository = mockk(relaxed = true)
        settingsRepository = mockk()

        every {
            settingsRepository.settings
        } returns settingsFlow

        viewModel = ChallengeViewModel(
            challengeRepository = challengeRepository,
            settingsRepository = settingsRepository
        )
    }

    @After
    fun tearDown() {
        unmockkStatic(SystemClock::class)
    }

    @Test
    fun shouldInitializeChallengeWithMediumDifficultyAndTwoMinuteCountdown() {
        val uiState = viewModel.uiState.value

        assertEquals(DifficultyOptions.MEDIUM, uiState.difficulty)
        assertEquals(120L, uiState.timer)
        assertEquals(16, uiState.cards.size)
    }

    @Test
    fun shouldNotStartCountdownBeforeFirstCardSelection() = runTest {
        val uiState = viewModel.uiState.value

        assertEquals(0, uiState.moveCount)
        assertEquals(120L, uiState.timer)

        advanceTimeBy(delayTime = 20_000.milliseconds)
        runCurrent()

        val updatedUiState = viewModel.uiState.value

        assertEquals(120L, updatedUiState.timer)
    }

    @Test
    fun shouldStartCountdownOnFirstValidCardSelection() = runTest {
        val uiState = viewModel.uiState.value

        val firstCard = uiState.cards.first()

        viewModel.onCardClick(
            selectedCard = firstCard
        )

        advanceTimeBy(delayTime = 20_000.milliseconds)
        runCurrent()

        val updatedUiState = viewModel.uiState.value

        assertEquals(100L, updatedUiState.timer)
    }

    @Test
    fun shouldNotSaveChallengeAttemptDateBeforeFirstCardSelection() {
        coVerify(exactly = 0) {
            challengeRepository.saveChallengeDate(any())
        }
    }

    @Test
    fun shouldSaveChallengeAttemptDateWhenChallengeStarts() = runTest {
        val uiState = viewModel.uiState.value

        val firstCard = uiState.cards.first()

        viewModel.onCardClick(
            selectedCard = firstCard
        )

        runCurrent()

        coVerify(exactly = 1) {
            challengeRepository.saveChallengeDate(any())
        }
    }

    @Test
    fun shouldSaveChallengeAttemptDateOnlyOnce() = runTest {
        coVerify(exactly = 0) {
            challengeRepository.saveChallengeDate(any())
        }

        val uiState = viewModel.uiState.value

        val firstCard = uiState.cards.first()
        val secondCard = uiState.cards.first {
            it.id != firstCard.id
        }

        viewModel.onCardClick(
            selectedCard = firstCard
        )

        viewModel.onCardClick(
            selectedCard = secondCard
        )

        runCurrent()

        coVerify(exactly = 1) {
            challengeRepository.saveChallengeDate(any())
        }
    }

    @Test
    fun shouldFailChallengeWhenCountdownReachesZero() = runTest {
        val uiState = viewModel.uiState.value

        val firstCard = uiState.cards.first()

        viewModel.onCardClick(
            selectedCard = firstCard
        )

        advanceTimeBy(delayTime = 120_000.milliseconds)
        runCurrent()

        val failedUiState = viewModel.uiState.value

        assertEquals(0L, failedUiState.timer)
        assertTrue(failedUiState.isFailed)

        advanceTimeBy(delayTime = 5_000.milliseconds)
        runCurrent()

        val finalUiState = viewModel.uiState.value

        assertEquals(0L, finalUiState.timer)
        assertTrue(finalUiState.isFailed)
    }

    @Test
    fun shouldNotAllowCountdownToBecomeNegative() = runTest {
        val uiState = viewModel.uiState.value

        assertEquals(120L, uiState.timer)

        viewModel.onCardClick(
            selectedCard = uiState.cards.first()
        )

        advanceTimeBy(delayTime = 130_000.milliseconds)
        runCurrent()

        val updatedUiState = viewModel.uiState.value

        assertEquals(0L, updatedUiState.timer)
        assertTrue(updatedUiState.isFailed)

        advanceTimeBy(delayTime = 10_000.milliseconds)
        runCurrent()

        val finalUiState = viewModel.uiState.value

        assertEquals(0L, finalUiState.timer)
    }

    @Test
    fun shouldCompleteChallengeWhenAllPairsAreMatched() = runTest {
        val state = viewModel.uiState.value
        val distinctSymbols = state.cards.map { it.symbol }.distinct()

        for (symbol in distinctSymbols) {
            val pairCards = state.cards.filter { it.symbol == symbol }

            viewModel.onCardClick(
                selectedCard = pairCards[0]
            )
            viewModel.onCardClick(
                selectedCard = pairCards[1]
            )

            runCurrent()
        }

        val updatedUiState = viewModel.uiState.value

        assertTrue(updatedUiState.isCompleted)
    }

    @Test
    fun shouldStopCountdownWhenChallengeIsCompleted() = runTest {
        val state = viewModel.uiState.value
        val distinctSymbols = state.cards.map { it.symbol }.distinct()

        assertEquals(120L, state.timer)

        for (symbol in distinctSymbols) {
            val pairCards = state.cards.filter { it.symbol == symbol }

            viewModel.onCardClick(
                selectedCard = pairCards[0]
            )
            viewModel.onCardClick(
                selectedCard = pairCards[1]
            )

            advanceTimeBy(delayTime = 1_000.milliseconds)
            runCurrent()
        }

        val completedUiState = viewModel.uiState.value
        assertTrue(completedUiState.isCompleted)

        val timerAtCompletion = completedUiState.timer

        advanceTimeBy(delayTime = 5_000.milliseconds)
        runCurrent()

        val finalUiState = viewModel.uiState.value

        assertEquals(timerAtCompletion, finalUiState.timer)
    }

    @Test
    fun shouldIgnoreCardInteractionsAfterChallengeIsCompleted() = runTest {
        val initialCards = viewModel.uiState.value.cards
        val distinctSymbols = initialCards.map { it.symbol }.distinct()

        for (symbol in distinctSymbols) {
            val pairCards = viewModel.uiState.value.cards
                .filter { it.symbol == symbol && !it.isRevealed }

            if (pairCards.size == 2) {
                viewModel.onCardClick(
                    selectedCard = pairCards[0]
                )

                viewModel.onCardClick(
                    selectedCard = pairCards[1]
                )
                runCurrent()
            }
        }

        val completedState = viewModel.uiState.value
        assertTrue(completedState.isCompleted)

        val cardsAtCompletion = completedState.cards
        val moveCountAtCompletion = completedState.moveCount
        val timerAtCompletion = completedState.timer

        viewModel.onCardClick(
            selectedCard = initialCards.first()
        )
        runCurrent()

        advanceTimeBy(delayTime = 5_000.milliseconds)
        runCurrent()

        val finalState = viewModel.uiState.value

        assertTrue(finalState.isCompleted)
        assertEquals(cardsAtCompletion, finalState.cards)
        assertEquals(moveCountAtCompletion, finalState.moveCount)
        assertEquals(timerAtCompletion, finalState.timer)
    }

    @Test
    fun shouldIgnoreCardInteractionsAfterChallengeHasFailed() = runTest {
        val uiState = viewModel.uiState.value

        viewModel.onCardClick(
            selectedCard = uiState.cards.first()
        )

        advanceTimeBy(delayTime = 120_000.milliseconds)
        runCurrent()

        val failedUiState = viewModel.uiState.value

        assertEquals(0L, failedUiState.timer)
        assertTrue(failedUiState.isFailed)

        val cardsAtFailure = failedUiState.cards
        val moveCountAtFailure = failedUiState.moveCount

        viewModel.onCardClick(
            selectedCard = failedUiState.cards.last()
        )

        val finalUiState = viewModel.uiState.value

        assertTrue(finalUiState.isFailed)
        assertEquals(0L, finalUiState.timer)
        assertEquals(cardsAtFailure, finalUiState.cards)
        assertEquals(moveCountAtFailure, finalUiState.moveCount)
    }

    @Test
    fun shouldNotProcessMismatchAfterChallengeHasFailed() = runTest {
        val state = viewModel.uiState.value

        val firstCard = state.cards.first()
        val secondCard = state.cards.first {
            it.symbol != firstCard.symbol
        }

        viewModel.onCardClick(
            selectedCard = firstCard
        )

        advanceTimeBy(delayTime = 119_500.milliseconds)
        runCurrent()

        viewModel.onCardClick(
            selectedCard = secondCard
        )

        runCurrent()

        advanceTimeBy(delayTime = 500.milliseconds)
        runCurrent()

        val failedState = viewModel.uiState.value

        assertEquals(0L, failedState.timer)
        assertTrue(failedState.isFailed)

        val cardsAtFailure = failedState.cards
        val moveCountAtFailure = failedState.moveCount

        advanceTimeBy(delayTime = 1_000.milliseconds)
        runCurrent()

        val finalState = viewModel.uiState.value

        assertTrue(finalState.isFailed)
        assertEquals(0L, finalState.timer)
        assertEquals(cardsAtFailure, finalState.cards)
        assertEquals(moveCountAtFailure, finalState.moveCount)
    }
}