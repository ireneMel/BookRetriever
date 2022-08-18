package com.example.bookretriever.repositories

import com.example.bookretriever.databases.dao.BooksDao
import com.example.bookretriever.models.Book
import com.example.bookretriever.models.BookEntity
import com.example.bookretriever.net.IBookClient
import com.example.bookretriever.utils.Constants.DELTA
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class BooksRepository @Inject constructor(
    private val bookDao: BooksDao,
    private val retrofit: IBookClient,
) {
    suspend fun getTrendingBooks(): BookEntity? {
        val first: BookEntity? = bookDao.getTrendingBooks().first().firstOrNull()
        val isCacheExpired = System.currentTimeMillis() - (first?.timeInMillis ?: 0) > DELTA

        if (first == null || isCacheExpired) {
            //if there is no appropriate data in the database - the request to the server is sent
            //retrieved data has to be saved
            val response = retrofit.getTrendingBooks()//.body()

            if (!response.isSuccessful) return null
            val bookResponse = response.body() ?: return null

            val list = bookResponse.works.map {
                Book(
                    it.isbn,
                    it.title,
                    it.authorName[0],
                    it.cover_i
                )
            }
            val finalEntity = BookEntity(System.currentTimeMillis(), list)
            bookDao.insert(finalEntity)
            return finalEntity

        } else {
            //if database contains needed data - get and show it
            return first
        }
    }

//    fun getBooksByTitleCached(title: String) = bookDao.getBookByTitle(title)
//    fun getTrendingBooksCached() = bookDao.getTrendingBooks()
}