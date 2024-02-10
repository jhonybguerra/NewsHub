package com.jbgcomposer.newshub.utils

import android.content.Context
import android.widget.Toast
import com.jbgcomposer.newshub.db.FavoriteArticle
import com.jbgcomposer.newshub.models.Article
import com.jbgcomposer.newshub.models.Source
import java.sql.Time
import java.text.MessageFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun String?.dateFormat(): String? {
    if (this == null) return this

    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val outputFormat = SimpleDateFormat("E d MMM yyyy", Locale.getDefault())

    return try {
        inputFormat.parse(this)?.let { outputFormat.format(it) } ?: this
    } catch (e: ParseException) {
        e.printStackTrace()
        this
    }
}

fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(context, message, duration).show()

fun fromArticleResponseToArticleEntity(article: Article) : FavoriteArticle =
    FavoriteArticle(
        author = article.author,
        content = article.content,
        description = article.description,
        publishedAt = article.publishedAt,
        source = article.source?.name,
        title = article.title,
        url = article.url,
        urlToImage = article.urlToImage
    )

fun fromArticleEntityToArticleResponse(favoriteArticle: FavoriteArticle) =
    Article(
        author = favoriteArticle.author,
        content = favoriteArticle.content,
        description = favoriteArticle.description,
        publishedAt = favoriteArticle.publishedAt,
        source = Source(name = favoriteArticle.source ?: "Not found"),
        title = favoriteArticle.title,
        url = favoriteArticle.url,
        urlToImage = favoriteArticle.urlToImage
    )