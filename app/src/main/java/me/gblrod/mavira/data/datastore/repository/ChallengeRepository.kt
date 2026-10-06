package me.gblrod.mavira.data.datastore.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.gblrod.mavira.data.datastore.maviraDataStore
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

private val lastChallengeDateKey = stringPreferencesKey(name = "last_challenge_date")

@Singleton
class ChallengeRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val lastChallengeDate: Flow<LocalDate?> =
        context.maviraDataStore.data.map { preferences ->
            preferences[lastChallengeDateKey]
                ?.let(block = LocalDate::parse)
        }

    suspend fun saveChallengeDate(date: LocalDate) {
        context.maviraDataStore.edit { preferences ->
            preferences[lastChallengeDateKey] = date.toString()
        }
    }
}