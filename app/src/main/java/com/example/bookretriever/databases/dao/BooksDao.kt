package com.example.bookretriever.databases.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.bookretriever.models.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BooksDao {

//    @Query("SELECT * FROM books WHERE title LIKE :title")
//    fun getBookByTitle(title: String = "%"): Flow<List<BookEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(bookEntity: BookEntity)

    @Query("SELECT * FROM books")
    fun getTrendingBooks(): Flow<List<BookEntity>>


}