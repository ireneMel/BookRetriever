package com.example.bookretriever.repositories

import com.example.bookretriever.databases.shelf.ShelfDao
import com.example.bookretriever.models.Book
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShelfRepository @Inject constructor(
    private val dao: ShelfDao
) {
    suspend fun insert(book: Book) = dao.insert(book)
    fun getAllBooks(): Flow<List<Book>> = dao.getAllBooks()
    suspend fun getBook(title: String) = dao.getBook(title)
    suspend fun deleteAll() = dao.deleteAll()
    suspend fun delete(book: Book) = dao.delete(book)
}