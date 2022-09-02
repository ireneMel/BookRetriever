package com.example.bookretriever.net

import com.example.bookretriever.models.json.BookResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IBookClient {

    @GET("/search/authors.json")
    suspend fun getBooksByAuthor(
        @Query("q") query: String,
        @Query("mode") queryMode: String = "everything"
    ): Response<BookResponse>

    @GET("/search.json")
    suspend fun getBookByTitle(
        @Query("title") query: String,
        @Query("mode") queryMode: String = "everything"
    ): Response<BookResponse>

    @GET("/trending/daily.json")
    suspend fun getTrendingBooks(@Query("page") page: Int, @Query("limit") pageSize: Int): Response<BookResponse>
}