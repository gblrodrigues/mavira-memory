package me.gblrod.mavira.data.datastore.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.gblrod.mavira.data.datastore.maviraDataStore
import me.gblrod.mavira.data.datastore.model.ChallengeDateState
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

private val lastChallengeDateKey = stringPreferencesKey(name = "last_challenge_date")

@Singleton
class ChallengeRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val repositoryScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    val challengeDateState: StateFlow<ChallengeDateState> =
        context.maviraDataStore.data
            .map { preferences ->
                ChallengeDateState.Loaded(
                    lastAttemptDate = preferences[lastChallengeDateKey]?.let(LocalDate::parse)
                )
            }
            .stateIn(
                scope = repositoryScope,
                started = SharingStarted.Eagerly,
                initialValue = ChallengeDateState.Loading
            )

    suspend fun saveChallengeDate(date: LocalDate) {
        context.maviraDataStore.edit { preferences ->
            preferences[lastChallengeDateKey] = date.toString()
        }
    }
}