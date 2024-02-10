package com.jbgcomposer.newshub

import com.jbgcomposer.newshub.api.NewsApiService
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class NewsApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: NewsApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `get headlines returns successful response`() = runBlocking {
        val jsonResponse = """
        {
            "status": "ok",
            "totalResults": 1,
            "articles": [
                {
                    "source": { "id": null, "name": "Example" },
                    "author": "Author",
                    "title": "Title",
                    "description": "Description",
                    "url": "http://example.com",
                    "urlToImage": "http://example.com/image.jpg",
                    "publishedAt": "2021-05-10T10:00:00Z",
                    "content": "Content"
                }
            ]
        }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(HttpURLConnection.HTTP_OK))
        val response = apiService.getHeadlines(countryCode = "us")
        assertTrue(response.isSuccessful)

        val responseBody = response.body()
        assertNotNull(responseBody)
        assertEquals("ok", responseBody?.status)
        assertEquals(1, responseBody?.totalResults)
        assertEquals("Title", responseBody?.articles?.first()?.title)
    }
}