package com.example.bookretriever.repositories

import com.example.bookretriever.databases.dao.BooksDao
import com.example.bookretriever.net.BookClientInterface

class BooksRepository(
//    private val bookDao: BooksDao
) {
    val retrofit = BookClientInterface.createClient()

//    suspend fun getTrendingBooks() = retrofit.getTrendingBooks()

//    fun getBooksByTitleCached(title: String) = bookDao.getBookByTitle(title)
//    fun getTrendingBooksCached() = bookDao.getTrendingBooks()
}