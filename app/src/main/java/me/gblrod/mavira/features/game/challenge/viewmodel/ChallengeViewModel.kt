package me.gblrod.mavira.features.game.challenge.viewmodel

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
import kotlinx.coroutines.launch
import me.gblrod.mavira.data.datastore.repository.ChallengeRepository
import me.gblrod.mavira.data.datastore.repository.SettingsRepository
import me.gblrod.mavira.features.game.challenge.state.ChallengeUiState
import me.gblrod.mavira.features.game.config.ChallengeConfig
import me.gblrod.mavira.features.game.difficulty.model.DifficultyOptions
import me.gblrod.mavira.features.game.engine.GameEngine
import me.gblrod.mavira.features.game.model.GameEffect
import me.gblrod.mavira.features.game.model.MemoryCard
import java.time.LocalDate
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ChallengeUiState(
            difficulty = DifficultyOptions.MEDIUM
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<GameEffect>()
    val effects = _effects.asSharedFlow()

    private var checkPairJob: Job? = null
    private var timerJob: Job? = null

    private val gameEngine = GameEngine()
    private val settings = requireNotNull(
        settingsRepository.settings.value
    )

    private val vibrationEnabled = settingsRepository.settings
        .filterNotNull()
        .map { it.vibrationEnabled }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = settings.vibrationEnabled
        )

    val isVibrationEnabled = vibrationEnabled

    init {
        startChallenge()
    }

    private fun startChallenge() {
        val state = gameEngine.startGame(
            pairs = DifficultyOptions.MEDIUM.pairs
        )

        _uiState.value = ChallengeUiState(
            difficulty = DifficultyOptions.MEDIUM,
            cards = state.cards,
            timer = ChallengeConfig.TIME_LIMIT_SECONDS
        )
    }

    fun onCardClick(selectedCard: MemoryCard) {
        val state = _uiState.value

        if (state.isCompleted || state.isFailed || state.isCheckingPair) return

        val result = gameEngine.selectCard(
            cardId = selectedCard.id
        )

        if (!result.accepted) return

        if (!state.isGameStarted) {
            viewModelScope.launch {
                challengeRepository.saveChallengeDate(
                    date = LocalDate.now()
                )
            }

            startTimer()

            _uiState.update {
                it.copy(isGameStarted = true)
            }
        }

        _uiState.update {
            it.copy(
                cards = result.state.cards,
                moveCount = result.state.moveCount
            )
        }

        if (result.pairReady) {
            checkPair()
        }
    }

    private fun checkPair() {
        val state = _uiState.value
        if (state.isCompleted || state.isFailed) return

        val result = gameEngine.checkPair() ?: return

        if (result.isMatch) {
            _uiState.update {
                it.copy(
                    cards = result.state.cards,
                    pairsFound = result.state.pairsFound
                )
            }

            if (result.isComplete) {
                timerJob?.cancel()
                timerJob = null

                _uiState.update {
                    it.copy(
                        isCompleted = true
                    )
                }

                viewModelScope.launch {
                    _effects.emit(GameEffect.GameCompleted)
                }
            } else {
                viewModelScope.launch {
                    _effects.emit(GameEffect.PairMatched)
                }
            }

            return
        }

        _uiState.update {
            it.copy(isCheckingPair = true)
        }

        checkPairJob?.cancel()

        checkPairJob = viewModelScope.launch {
            delay(duration = 1_000.milliseconds)

            if (_uiState.value.isCompleted || _uiState.value.isFailed) return@launch

            val updatedCards = gameEngine.hidePair(
                firstCardId = result.firstCardId,
                secondCardId = result.secondCardId
            ).cards

            _uiState.update {
                it.copy(
                    cards = updatedCards,
                    isCheckingPair = false
                )
            }

            checkPairJob = null
        }
    }

    private fun startTimer() {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            while (true) {
                delay(duration = 1_000.milliseconds)

                val state = _uiState.value
                if (state.isCompleted || state.isFailed) break

                val remainingTime = (state.timer - 1L).coerceAtLeast(minimumValue = 0L)

                _uiState.update {
                    it.copy(
                        timer = remainingTime,
                        isFailed = remainingTime == 0L,
                        isCheckingPair = remainingTime != 0L && it.isCheckingPair
                    )
                }

                if (remainingTime == 0L) {
                    checkPairJob?.cancel()
                    checkPairJob = null
                    break
                }
            }

            timerJob = null
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        checkPairJob?.cancel()
    }
}