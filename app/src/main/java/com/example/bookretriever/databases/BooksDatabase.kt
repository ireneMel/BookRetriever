package com.example.bookretriever.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bookretriever.databases.dao.BooksDao
import com.example.bookretriever.models.BookEntity

@Database(entities = [BookEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BooksDatabase : RoomDatabase() {
    abstract fun getDao(): BooksDao
}