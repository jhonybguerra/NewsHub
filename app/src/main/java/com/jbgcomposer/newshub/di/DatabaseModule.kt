package com.jbgcomposer.newshub.di

import android.content.Context
import androidx.room.Room
import com.jbgcomposer.newshub.db.AppDatabase
import com.jbgcomposer.newshub.db.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ) : AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providesFavoriteDao(database: AppDatabase) : FavoriteDao {
        return database.favoriteDao()
    }
}