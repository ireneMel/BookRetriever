package com.example.bookretriever.databases.shelf

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.bookretriever.models.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface ShelfDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(book: Book)

    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<Book>>

    @Query("DELETE FROM books")
    suspend fun deleteAll()
}