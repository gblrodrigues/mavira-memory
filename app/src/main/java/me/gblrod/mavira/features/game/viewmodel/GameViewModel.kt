package me.gblrod.mavira.features.game.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    fun onCardClick(selectedCard: MemoryCard) {
        val currentState = _uiState.value

        if (
            currentState.isCheckingPair ||
            selectedCard.isRevealed ||
            selectedCard.isMatched
        ) {
            return
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

        viewModelScope.launch {
            if (firstCard.item == secondCard.item) {
                _uiState.update { state ->
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
                        pairsFound = state.pairsFound + 1,
                        isCheckingPair = false
                    )
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

    fun restart() {
        _uiState.value = GameUiState(
            cards = createDeck()
        )
    }
}