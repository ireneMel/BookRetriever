package com.example.bookretriever.databases.trending

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.bookretriever.models.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BooksDao {

//    @Query("SELECT * FROM books WHERE title LIKE :title")
//    fun getBookByTitle(title: String = "%"): Flow<List<BookEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(bookEntity: BookEntity)

    @Query("DELETE FROM books")
    fun deleteAll()

    @Query("SELECT * FROM books")
    fun getTrendingBooks(): Flow<List<BookEntity>>
}