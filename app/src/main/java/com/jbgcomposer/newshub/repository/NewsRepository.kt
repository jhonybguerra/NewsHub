package com.jbgcomposer.newshub.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jbgcomposer.newshub.api.NewsApiService
import com.jbgcomposer.newshub.db.AppDatabase
import com.jbgcomposer.newshub.db.FavoriteArticle
import com.jbgcomposer.newshub.models.Article
import com.jbgcomposer.newshub.paging.HeadlinesPagingSource
import com.jbgcomposer.newshub.paging.SearchPagingSource
import com.jbgcomposer.newshub.utils.fromArticleResponseToArticleEntity
import kotlinx.coroutines.flow.Flow
import java.util.Locale.IsoCountryCode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    val apiService: NewsApiService,
    val db: AppDatabase
) {

    fun getHeadlines(countryCode: String) : Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = { HeadlinesPagingSource(apiService, countryCode) }
        ).flow
    }

    fun searchNews(searchQuery: String) : Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = { SearchPagingSource(apiService, searchQuery) }
        ).flow
    }

    suspend fun upsert(article: Article) =
        db.favoriteDao().upsert(fromArticleResponseToArticleEntity(article))

    fun getSavedNews() = db.favoriteDao().getSavedNews()

    suspend fun deleteArticle(favoriteArticle: FavoriteArticle) =
        db.favoriteDao().deleteArticle(favoriteArticle)

}