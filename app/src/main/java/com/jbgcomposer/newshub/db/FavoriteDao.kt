package com.jbgcomposer.newshub.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(favoriteArticle: FavoriteArticle) : Long

    @Query("SELECT * FROM articles")
    fun getSavedNews(): LiveData<List<FavoriteArticle>>

    @Delete
    suspend fun deleteArticle(favoriteArticle: FavoriteArticle)
}