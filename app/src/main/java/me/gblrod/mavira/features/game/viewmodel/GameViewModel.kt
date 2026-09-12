package me.gblrod.mavira.features.game.viewmodel

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.gblrod.mavira.features.game.model.MemoryCard
import me.gblrod.mavira.features.game.model.createDeck
import me.gblrod.mavira.features.game.state.GameUiState
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class GameViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState = _uiState.asStateFlow()

    private var gameStartedAt: Long? = null
    private var timerJob: Job? = null
    private var checkPairJob: Job? = null

    fun onCardClick(selectedCard: MemoryCard) {
        val currentState = _uiState.value

        if (
            currentState.isCheckingPair ||
            selectedCard.isRevealed ||
            selectedCard.isMatched
        ) {
            return
        }

        if (gameStartedAt == null) {
            startTimer()
        }

        _uiState.update {
            it.copy(
                isGameStarted = true
            )
        }

        val revealedCards = currentState.cards.filter {
            it.isRevealed && !it.isMatched
        }

        val updatedCards = currentState.cards.map { card ->
            if (card.id == selectedCard.id) {
                card.copy(
                    isRevealed = true
                )
            } else {
                card
            }
        }

        _uiState.update {
            it.copy(
                cards = updatedCards
            )
        }

        if (revealedCards.size == 1) {
            checkPair(
                firstCard = revealedCards.first(),
                secondCard = selectedCard
            )
        }
    }

    private fun checkPair(
        firstCard: MemoryCard,
        secondCard: MemoryCard
    ) {
        _uiState.update {
            it.copy(
                moveCount = it.moveCount + 1,
                isCheckingPair = true
            )
        }

        checkPairJob = viewModelScope.launch {
            if (firstCard.item == secondCard.item) {

                _uiState.update { state ->
                    val newPairsFound = state.pairsFound + 1
                    state.copy(
                        cards = state.cards.map { card ->
                            if (card.id == firstCard.id || card.id == secondCard.id) {
                                card.copy(
                                    isMatched = true
                                )
                            } else {
                                card
                            }
                        },
                        pairsFound = newPairsFound,
                        isCheckingPair = false
                    )
                }

                if (_uiState.value.pairsFound == _uiState.value.pairsTotal) {
                    stopTimer()
                }
            } else {
                delay(duration = 1_000.milliseconds)

                _uiState.update { state ->
                    state.copy(
                        cards = state.cards.map { card ->
                            if (card.id == firstCard.id || card.id == secondCard.id) {
                                card.copy(
                                    isRevealed = false
                                )
                            } else {
                                card
                            }
                        },
                        isCheckingPair = false
                    )
                }
            }
        }
    }

    private fun startTimer() {
        if (gameStartedAt != null) return

        gameStartedAt = SystemClock.elapsedRealtime()

        timerJob = viewModelScope.launch {
            while (isActive) {
                val startTime = gameStartedAt ?: break

                val elapsed = (SystemClock.elapsedRealtime() - startTime) / 1_000

                _uiState.update {
                    it.copy(
                        timer = elapsed
                    )
                }

                delay(duration = 250.milliseconds)
            }
        }
    }

    private fun stopTimer() {
        val startTime = gameStartedAt ?: return

        val elapsed = (SystemClock.elapsedRealtime() - startTime) / 1_000

        timerJob?.cancel()
        timerJob = null

        _uiState.update {
            it.copy(
                timer = elapsed
            )
        }
    }

    fun restart() {
        timerJob?.cancel()
        timerJob = null

        checkPairJob?.cancel()
        checkPairJob = null

        gameStartedAt = null

        _uiState.value = GameUiState(
            cards = createDeck()
        )
    }
}