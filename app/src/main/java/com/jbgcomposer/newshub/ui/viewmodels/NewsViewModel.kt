package com.jbgcomposer.newshub.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jbgcomposer.newshub.db.FavoriteArticle
import com.jbgcomposer.newshub.models.Article
import com.jbgcomposer.newshub.repository.NewsRepository
import com.jbgcomposer.newshub.utils.NetworkStatusChecker
import com.jbgcomposer.newshub.utils.fromArticleEntityToArticleResponse
import com.jbgcomposer.newshub.utils.fromArticleResponseToArticleEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    val newsRepository: NewsRepository,
    private val networkStatusChecker: NetworkStatusChecker
) : ViewModel() {

    private val countryCode = Locale.getDefault().country ?: "us"
    private val _searchQuery = MutableStateFlow("")

    //Remote
    val headlinesPagingDataFlow: Flow<PagingData<Article>> =
        if(networkStatusChecker.hasInternetConnection()) {
            newsRepository.getHeadlines(countryCode).cachedIn(viewModelScope)
        } else {
            flow {emit(PagingData.empty())}
        }

    val searchPagingDataFlow: Flow<PagingData<Article>> = _searchQuery
        .filter { it.isNotBlank() }
        .flatMapLatest { query ->
            if(networkStatusChecker.hasInternetConnection()) {
                newsRepository.searchNews(query).cachedIn(viewModelScope)
            } else {
                flow { emit(PagingData.empty()) }
            }
        }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    //Local

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(favoriteArticle: FavoriteArticle) = viewModelScope.launch {
        newsRepository.deleteArticle(favoriteArticle)
    }

    fun convertFavoriteArticleToArticle(favoriteArticle: FavoriteArticle): Article {
        return fromArticleEntityToArticleResponse(favoriteArticle)
    }
}