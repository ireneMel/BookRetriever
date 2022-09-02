package com.example.bookretriever.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.bookretriever.models.Book
import com.example.bookretriever.repositories.BooksRepository
import javax.inject.Inject

class BookPagingSource @Inject constructor(
    private val repository: BooksRepository
) : PagingSource<Int, Book>() {

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        return try {
            val position = params.key ?: 1
            val response =
                repository.getTrendingBooks(position, params.loadSize)?.response ?: emptyList()
            Log.d("NO RESPONSE Book paging source", "load: ${response.toString()}")
            return LoadResult.Page(
                response,
                if (position == 1) null else position - 1,
                if (response.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


}