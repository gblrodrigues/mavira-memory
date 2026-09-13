package me.gblrod.mavira.data.room.mapper

import me.gblrod.mavira.data.room.local.entity.GameResultEntity
import me.gblrod.mavira.features.game.model.GameResult

fun GameResultEntity.toDomain(): GameResult {
    return GameResult(
        id = id,
        moves = moves,
        time = time,
        playedAt = playedAt
    )
}

fun GameResult.toEntity(): GameResultEntity {
    return GameResultEntity(
        id = id,
        moves = moves,
        time = time,
        playedAt = playedAt
    )
}