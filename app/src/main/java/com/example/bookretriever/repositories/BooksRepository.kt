package com.example.bookretriever.repositories

import com.example.bookretriever.databases.trending.BooksDao
import com.example.bookretriever.models.Book
import com.example.bookretriever.models.BookEntity
import com.example.bookretriever.models.json.BookResponse
import com.example.bookretriever.net.IBookClient
import com.example.bookretriever.utils.Constants.DELTA
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BooksRepository @Inject constructor(
    private val bookDao: BooksDao,
    private val retrofit: IBookClient
) {
    suspend fun getBookByQuery(q: String, page: Int, pageSize: Int): List<Book> {
        val response = retrofit.getBookByQuery(q, page, pageSize)
        if (!response.isSuccessful) return emptyList()
        return mapResponse(response.body() ?: return emptyList())
    }

    suspend fun getTrendingBooks(page: Int, pageSize: Int): BookEntity? = withContext(Dispatchers.IO){
            val first: BookEntity? = bookDao.getTrendingBooks().first().firstOrNull()
            val isCacheExpired = System.currentTimeMillis() - (first?.timeInMillis ?: 0) > DELTA
            if (first == null || isCacheExpired) {
                bookDao.deleteAll()
                //if there is no appropriate data in the database - the request to the server is sent
                //retrieved data has to be saved
                val response = retrofit.getTrendingBooks(page, pageSize)

                if (!response.isSuccessful) return@withContext null
                val bookResponse = response.body() ?: return@withContext null
                val list = mapResponse(bookResponse)
                val finalEntity = BookEntity(System.currentTimeMillis(), list)
                bookDao.insert(finalEntity)
                return@withContext finalEntity
            } else {
                //if database contains needed data - get and show it
                return@withContext first
            }
        }

    private fun listToString(authors: List<String>?): String {
        var res = ""
        if (authors == null) return res
        for (author in 0..authors.size - 2) {
            res += "$author, "
        }
        res += authors[authors.size - 1]
        return res
    }

    private fun mapResponse(bookResponse: BookResponse): List<Book> = bookResponse.works.map {
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
}