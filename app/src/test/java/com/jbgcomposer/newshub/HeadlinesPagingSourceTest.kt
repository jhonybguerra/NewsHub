package com.jbgcomposer.newshub

import androidx.paging.PagingSource
import com.jbgcomposer.newshub.api.NewsApiService
import com.jbgcomposer.newshub.models.Article
import com.jbgcomposer.newshub.models.NewsResponse
import com.jbgcomposer.newshub.models.Source
import com.jbgcomposer.newshub.paging.HeadlinesPagingSource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.HttpException
import retrofit2.Response

class HeadlinesPagingSourceTest {

    private lateinit var newsApiService: NewsApiService
    private lateinit var headlinesPagingSource: HeadlinesPagingSource

    @Before
    fun setup() {
        newsApiService = mock(NewsApiService::class.java)
        headlinesPagingSource = HeadlinesPagingSource(newsApiService, "us")
    }

    @Test
    fun loadReturnsPageWhenOnSuccessfulLoadOfItems() = runTest {
        val mockResponse = NewsResponse(
            articles = mutableListOf(
                Article(
                    author = "Author 1",
                    content = "Content of the first article.",
                    description = "Description of the first article.",
                    publishedAt = "2021-01-01T00:00:00Z",
                    source = Source(name = "Source 1"),
                    title = "Title of the first article",
                    url = "http://www.example.com/first-article",
                    urlToImage = "http://www.example.com/images/first-article.jpg"
                ),
                Article(
                    author = "Author 2",
                    content = "Content of the second article.",
                    description = "Description of the second article.",
                    publishedAt = "2021-01-02T00:00:00Z",
                    source = Source(name = "Source 2"),
                    title = "Title of the second article",
                    url = "http://www.example.com/second-article",
                    urlToImage = "http://www.example.com/images/second-article.jpg"
                )
            ),
            status = "ok",
            totalResults = 2
        )

        `when`(newsApiService.getHeadlines("us", 1))
            .thenReturn(Response.success(mockResponse))

        val loadResult = headlinesPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        assertTrue(loadResult is PagingSource.LoadResult.Page)

        val page = loadResult as PagingSource.LoadResult.Page
        assertEquals(mockResponse.articles, page.data)
        assertNull(page.prevKey)
        assertEquals(2, page.nextKey)
    }

    @Test
    fun loadReturnsErrorWhenApiServiceFails() = runTest {
        val errorResponseBody = "Error response".toResponseBody("application/json".toMediaTypeOrNull())
        val errorResponse = Response.error<Any>(400, errorResponseBody)

        `when`(newsApiService.getHeadlines(countryCode = "us", pageNumber = 1))
            .thenThrow(HttpException(errorResponse))

        val loadResult = headlinesPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )
        assertTrue(loadResult is PagingSource.LoadResult.Error)
    }
}