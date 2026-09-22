package me.gblrod.mavira.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import me.gblrod.mavira.data.room.local.dao.GameResultDao
import me.gblrod.mavira.data.room.local.database.MaviraDatabase
import me.gblrod.mavira.data.room.migration.MIGRATION_1_2

@Module
@InstallIn(SingletonComponent::class)
object AppModules {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MaviraDatabase {
        return Room.databaseBuilder(
            name = "mavira.db",
            context = context,
            klass = MaviraDatabase::class.java
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    fun provideGameResultDao(
        database: MaviraDatabase
    ): GameResultDao = database.gameResultDao()
}