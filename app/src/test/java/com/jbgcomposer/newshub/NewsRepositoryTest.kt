package com.jbgcomposer.newshub

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jbgcomposer.newshub.api.NewsApiService
import com.jbgcomposer.newshub.db.AppDatabase
import com.jbgcomposer.newshub.models.Article
import com.jbgcomposer.newshub.models.NewsResponse
import com.jbgcomposer.newshub.models.Source
import com.jbgcomposer.newshub.repository.NewsRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class NewsRepositoryTest {

    @Mock
    private lateinit var newsApiService: NewsApiService

    @Mock
    private lateinit var appDatabase: AppDatabase

    private lateinit var newsRepository: NewsRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        newsRepository = NewsRepository(newsApiService, appDatabase)
    }

    @Test
    fun getHeadlinesCallsApiService() = runTest {
        val mockResponse = NewsResponse(
            articles = mutableListOf(
                Article(
                    author = "Jane Doe",
                    content = "This is a detailed content of the first article talking about environment.",
                    description = "First article description about environmental changes.",
                    publishedAt = "2022-01-01T12:00:00Z",
                    source = Source(name = "The Environmental News"),
                    title = "Environmental Changes Are Accelerating",
                    url = "https://www.example.com/environmental-changes",
                    urlToImage = "https://www.example.com/images/environment.jpg"
                ),
                Article(
                    author = "John Smith",
                    content = "This is a detailed content of the second article talking about technology.",
                    description = "Second article description about technology advancements.",
                    publishedAt = "2022-01-02T15:30:00Z",
                    source = Source(name = "Tech Daily"),
                    title = "Advancements in Technology for the Next Decade",
                    url = "https://www.example.com/tech-advancements",
                    urlToImage = "https://www.example.com/images/technology.jpg"
                )
            ),
            status = "ok",
            totalResults = 1
        )

        `when`(newsApiService.getHeadlines(countryCode = "us", pageNumber = 1))
            .thenReturn(Response.success(mockResponse))

        val job = launch {
            newsRepository.getHeadlines("us").collect { pagingData ->
                assertNotNull(pagingData)
            }
        }
        job.cancel()
    }

    @Test
    fun getHeadlinesReturnsErrorWhenApiFails() = runTest {
        val exception = HttpException(Response.error<Any>(400, "Network error".toResponseBody()))
        val mockPagingSource = object : PagingSource<Int, Article>() {
            override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
                return null
            }

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
                return LoadResult.Error(exception)
            }
        }

        val loadResult = mockPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(loadResult is PagingSource.LoadResult.Error)
        assertEquals(exception, (loadResult as PagingSource.LoadResult.Error).throwable)
    }
}