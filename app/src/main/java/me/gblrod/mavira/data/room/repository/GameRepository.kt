package me.gblrod.mavira.data.room.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.gblrod.mavira.data.room.local.dao.GameResultDao
import me.gblrod.mavira.data.room.local.entity.GameResultEntity
import me.gblrod.mavira.data.room.mapper.toDomain
import me.gblrod.mavira.data.room.mapper.toEntity
import me.gblrod.mavira.features.game.model.GameResult
import javax.inject.Inject

class GameRepository @Inject constructor(
    private val gameResultDao: GameResultDao
) {
    suspend fun saveResult(result: GameResult) {
        gameResultDao.insert(result = result.toEntity())
    }

    fun observeResults(): Flow<List<GameResult>> {
        return gameResultDao
            .observeAll()
            .map { results ->
                results.map(transform = GameResultEntity::toDomain)
            }
    }

    fun observeRecentGames(limit: Int): Flow<List<GameResult>> {
        return gameResultDao
            .observeRecent(limit = limit)
            .map { results ->
                results.map(transform = GameResultEntity::toDomain)
            }
    }
}