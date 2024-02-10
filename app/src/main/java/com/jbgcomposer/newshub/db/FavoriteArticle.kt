package com.jbgcomposer.newshub.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class FavoriteArticle(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: String?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
)