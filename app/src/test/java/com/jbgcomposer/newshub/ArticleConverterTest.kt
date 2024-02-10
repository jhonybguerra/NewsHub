package com.jbgcomposer.newshub

import com.jbgcomposer.newshub.db.FavoriteArticle
import com.jbgcomposer.newshub.models.Article
import com.jbgcomposer.newshub.utils.fromArticleResponseToArticleEntity
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ArticleConverterTest {

    @Test
    fun `convert Article to FavoriteArticle correctly`() {
        val article = Article(
            author = "CNN",
            content = "content",
            description = "description",
            publishedAt = "date",
            source = null,
            title = "title",
            url = "www",
            urlToImage = "www"
        )

        val expectedReponse = FavoriteArticle(
            author = "CNN",
            content = "content",
            description = "description",
            publishedAt = "date",
            source = null,
            title = "title",
            url = "www",
            urlToImage = "www"
        )

        val result = fromArticleResponseToArticleEntity(article)

        assertEquals(expectedReponse, result)
    }
}