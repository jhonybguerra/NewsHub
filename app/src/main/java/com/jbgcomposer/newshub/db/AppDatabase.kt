package com.jbgcomposer.newshub.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteArticle::class], version = 1, exportSchema = false )
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}