package com.example.bookretriever.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.bookretriever.models.BookWishlistModel

@Dao
interface BookWishlistDAO {

    @Insert(onConflict = REPLACE)
    suspend fun insert(book: BookWishlistModel)

    @Delete
    suspend fun delete(book: BookWishlistModel)

    @Query("DELETE FROM books_wishlist WHERE id = :idInput")
    fun delete(idInput: Int)

    @Query("DELETE FROM books_wishlist")
    fun deleteAll()

    @Query("SELECT * FROM books_wishlist")
    fun getAll(): LiveData<List<BookWishlistModel>>
}