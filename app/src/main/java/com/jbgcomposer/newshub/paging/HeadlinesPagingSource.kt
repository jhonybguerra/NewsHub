package com.jbgcomposer.newshub.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jbgcomposer.newshub.api.NewsApiService
import com.jbgcomposer.newshub.models.Article

class HeadlinesPagingSource(
    private val apiService: NewsApiService,
    private val countryCode: String
) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1) }
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val pageNumber = params.key ?: 1
        return try {
            val response = apiService.getHeadlines(countryCode, pageNumber)
            val articles = response.body()?.articles?.filterNot { it.title == "[Removed]" } ?: emptyList()
            LoadResult.Page(
                data = articles,
                prevKey = if(pageNumber == 1) null else pageNumber - 1,
                nextKey = if(articles.isEmpty()) null else pageNumber + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}