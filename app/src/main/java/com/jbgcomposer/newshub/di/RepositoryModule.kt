package com.jbgcomposer.newshub.di

import com.jbgcomposer.newshub.api.NewsApiService
import com.jbgcomposer.newshub.db.AppDatabase
import com.jbgcomposer.newshub.repository.NewsRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    fun provideNewsRepository(
        apiService: NewsApiService,
        db: AppDatabase
    ): NewsRepository {
        return NewsRepository(apiService, db)
    }
}