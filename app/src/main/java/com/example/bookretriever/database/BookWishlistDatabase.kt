package com.example.bookretriever.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bookretriever.models.BookWishlistModel

@Database(entities = [BookWishlistModel::class], version = 1, exportSchema = false)
abstract class BookWishlistDatabase : RoomDatabase() {
    abstract fun getDao(): BookWishlistDAO
}