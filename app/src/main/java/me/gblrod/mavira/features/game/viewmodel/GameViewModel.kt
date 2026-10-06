package me.gblrod.mavira.features.game.viewmodel

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.gblrod.mavira.data.datastore.repository.SettingsRepository
import me.gblrod.mavira.data.room.repository.GameRepository
import me.gblrod.mavira.features.game.config.HintConfig
import me.gblrod.mavira.features.game.engine.GameEngine
import me.gblrod.mavira.features.game.model.GameEffect
import me.gblrod.mavira.features.game.model.GameResult
import me.gblrod.mavira.features.game.model.MemoryCard
import me.gblrod.mavira.features.game.state.GameUiState
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {
    private val _effects = MutableSharedFlow<GameEffect>()
    val effects = _effects.asSharedFlow()

    private var gameStartedAt: Long? = null
    private var timerJob: Job? = null
    private var checkPairJob: Job? = null
    private var hintJob: Job? = null

    private val gameEngine = GameEngine()

    private val settings = requireNotNull(
        settingsRepository.settings.value
    )

    private val _uiState = MutableStateFlow(
        GameUiState(
            difficulty = settings.difficultyOptions,
            cards = gameEngine.startGame(
                pairs = settings.difficultyOptions.pairs
            ).cards
        )
    )

    val uiState = _uiState.asStateFlow()

    private val vibrationEnabled = settingsRepository.settings
        .filterNotNull()
        .map { it.vibrationEnabled }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = settings.vibrationEnabled
        )

    val isVibrationEnabled = vibrationEnabled


    fun onCardClick(selectedCard: MemoryCard) {
        if (_uiState.value.isCheckingPair) return

        val result = gameEngine.selectCard(
            cardId = selectedCard.id
        )

        if (!result.accepted) return

        if (gameStartedAt == null) {
            startTimer()
        }

        _uiState.update {
            it.copy(
                cards = result.state.cards,
                moveCount = result.state.moveCount,
                pairsFound = result.state.pairsFound,
                isGameStarted = true
            )
        }

        if (result.pairReady) {
            checkPair()
        }
    }

    fun onHintClick() {
        val state = _uiState.value

        if (!state.isGameStarted ||
            state.pairsFound == state.difficulty.pairs ||
            state.hintsRemaining <= 0 ||
            state.isCheckingPair ||
            state.isHintShowing
        ) return

        val hint = gameEngine.getHint() ?: return

        if (gameStartedAt == null) {
            startTimer()
        }

        _uiState.update {
            it.copy(
                hintsRemaining = it.hintsRemaining - 1,
                isHintShowing = true,
                isGameStarted = true,
                cards = it.cards.map { card ->
                    if (card.id in hint.cardIds) {
                        card.copy(
                            isGlowing = true
                        )
                    } else {
                        card
                    }
                }
            )
        }

        hintJob = viewModelScope.launch {
            delay(duration = HintConfig.HINT_GLOW_DURATION_MS.milliseconds)

            _uiState.update {
                it.copy(
                    isHintShowing = false,
                    cards = it.cards.map { card ->
                        if (card.id in hint.cardIds) {
                            card.copy(
                                isGlowing = false
                            )
                        } else {
                            card
                        }
                    }
                )
            }
        }
    }

    private fun checkPair() {
        val result = gameEngine.checkPair() ?: return

        _uiState.update {
            it.copy(
                isCheckingPair = true
            )
        }

        checkPairJob = viewModelScope.launch {
            if (result.isMatch) {
                _uiState.update {
                    it.copy(
                        cards = result.state.cards,
                        pairsFound = result.state.pairsFound,
                        isCheckingPair = false
                    )
                }

                if (result.isComplete) {
                    _effects.emit(GameEffect.GameCompleted)
                    stopTimer()
                    saveGameResult()
                } else {
                    _effects.emit(GameEffect.PairMatched)
                }

                return@launch
            }

            delay(duration = 1_000.milliseconds)

            val state = gameEngine.hidePair(
                firstCardId = result.firstCardId,
                secondCardId = result.secondCardId
            )

            _uiState.update {
                it.copy(
                    cards = state.cards,
                    isCheckingPair = false
                )
            }
        }
    }

    private fun saveGameResult() {
        val state = _uiState.value

        viewModelScope.launch {
            gameRepository.saveResult(
                GameResult(
                    moves = state.moveCount,
                    time = state.timer,
                    playedAt = System.currentTimeMillis(),
                    difficulty = state.difficulty
                )
            )
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

        hintJob?.cancel()
        hintJob = null

        gameStartedAt = null

        val difficulty = _uiState.value.difficulty
        val gameState = gameEngine.restart()

        _uiState.value = GameUiState(
            difficulty = difficulty,
            cards = gameState.cards,
            hintsRemaining = HintConfig.INITIAL_HINTS
        )
    }
}