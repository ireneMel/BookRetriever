package com.example.bookretriever.repositories

import com.example.bookretriever.databases.trending.BooksDao
import com.example.bookretriever.models.Book
import com.example.bookretriever.models.BookEntity
import com.example.bookretriever.net.IBookClient
import com.example.bookretriever.utils.Constants.DELTA
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class BooksRepository @Inject constructor(
    private val bookDao: BooksDao,
    private val retrofit: IBookClient
) {
    suspend fun getTrendingBooks(page: Int, pageSize: Int): BookEntity? {
        val first: BookEntity? = bookDao.getTrendingBooks().first().firstOrNull()
        val isCacheExpired = System.currentTimeMillis() - (first?.timeInMillis ?: 0) > DELTA
        if (first == null || isCacheExpired) {
            bookDao.deleteAll()
            //if there is no appropriate data in the database - the request to the server is sent
            //retrieved data has to be saved
            val response = retrofit.getTrendingBooks(page, pageSize)

            if (!response.isSuccessful) return null
            val bookResponse = response.body() ?: return null

            val list = bookResponse.works.map {
                Book(
//                    it.isbn,
                    listToString(it.authorName),
                    it.title,
//                    2002,
                    it.cover_i ?: "",
                    false,
//                    ""
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

    private fun listToString(authors: List<String>?): String {
        var res = ""
        if(authors == null) return res
        for (author in 0..authors.size - 2) {
            res += "$author, "
        }
        res += authors[authors.size - 1]
        return res
    }
}