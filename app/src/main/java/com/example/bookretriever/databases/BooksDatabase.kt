package com.example.bookretriever.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bookretriever.databases.dao.BooksDao
import com.example.bookretriever.models.Book

@Database(entities = [Book::class], version = 1, exportSchema = false)
abstract class BooksDatabase : RoomDatabase() {
    abstract fun getDao(): BooksDao
}