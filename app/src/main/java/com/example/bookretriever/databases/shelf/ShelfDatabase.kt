package com.example.bookretriever.databases.shelf

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bookretriever.databases.Converters
import com.example.bookretriever.models.Book
import com.example.bookretriever.models.BookEntity

@Database(entities = [Book::class], version = 6, exportSchema = false)
//@TypeConverters(ShelfConverters::class)
abstract class ShelfDatabase : RoomDatabase() {
    abstract fun getDao(): ShelfDao
}