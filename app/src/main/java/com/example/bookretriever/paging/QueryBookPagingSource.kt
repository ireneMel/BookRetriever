package com.example.bookretriever.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.bookretriever.models.Book
import com.example.bookretriever.repositories.BooksRepository
import javax.inject.Inject

class QueryBookPagingSource @Inject constructor(
    private val repository: BooksRepository,
    private val query: String
) : PagingSource<Int, Book>() {

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        return try {

            if (query.isEmpty())
                return LoadResult.Page(emptyList(), null, null)

            val position = params.key ?: 1
            println("Load $query, size = ${params.loadSize}, pos = $position")
            val response =
                repository.getBookByQuery(query, position, params.loadSize) ?: emptyList()
            println("Loaded $query, size = ${params.loadSize}, pos = $position")

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