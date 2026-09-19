package me.gblrod.mavira.data.room.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.gblrod.mavira.data.room.local.entity.GameResultEntity

@Dao
interface GameResultDao {
    @Insert
    suspend fun insert(result: GameResultEntity)

    @Query(
        value = """
        SELECT * FROM game_results
        ORDER BY playedAt DESC, id DESC
    """
    )

    fun observeAll(): Flow<List<GameResultEntity>>

    @Query(
        """
        SELECT * FROM game_results
        ORDER BY playedAt DESC, id DESC
        LIMIT :limit
        """
    )
    fun observeRecent(limit: Int): Flow<List<GameResultEntity>>
}