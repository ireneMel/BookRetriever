package com.example.bookretriever.databases.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.bookretriever.models.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BooksDao {

    @Query("SELECT * FROM books WHERE title LIKE :title")
    fun getBookByTitle(title: String = "%"): Flow<List<Book>>

    @Query("SELECT * FROM books")
    fun getTrendingBooks(): Flow<List<Book>>
}