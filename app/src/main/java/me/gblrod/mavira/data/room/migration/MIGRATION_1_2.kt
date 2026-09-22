package me.gblrod.mavira.data.room.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            ALTER TABLE game_results
            ADD COLUMN difficulty TEXT NOT NULL DEFAULT 'EASY'
            """
        )
    }
}