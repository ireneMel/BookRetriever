package com.example.bookretriever.ui.trending

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.bookretriever.models.Book
import com.example.bookretriever.models.UIBook
import com.example.bookretriever.paging.QueryBookPagingSource
import com.example.bookretriever.paging.TrendingBookPagingSource
import com.example.bookretriever.repositories.BooksRepository
import com.example.bookretriever.repositories.ShelfRepository
import com.example.bookretriever.utils.Constants.QUERY_LIMIT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
    private val favoritesRepository: ShelfRepository
) : ViewModel() {
    private val queryFlow = MutableStateFlow<String?>(null)
    private var prevSource: PagingSource<*, *>? = null

    val bookFlow = queryFlow.flatMapLatest {
        prevSource?.invalidate()
        if (it == null) getTrendingBooks()
        else getBooksByQuery(it)
    }

    fun querySubmitted(q: String) {
        queryFlow.value = q
    }

    private fun getTrendingBooks(): Flow<PagingData<UIBook>> {
        val pager = Pager(
            PagingConfig(
                pageSize = QUERY_LIMIT,
                prefetchDistance = 10,
                enablePlaceholders = false,
                initialLoadSize = QUERY_LIMIT * 3,
                maxSize = 50
            ),
            1
        ) {
            TrendingBookPagingSource(booksRepository).also { prevSource = it }
        }.flow.map {
            mapPagingData(it)
        }.cachedIn(viewModelScope)
        return pager
    }

    private fun getBooksByQuery(query: String): Flow<PagingData<UIBook>> {
        val pager = Pager(
            PagingConfig(
                pageSize = QUERY_LIMIT,
                prefetchDistance = 10,
                enablePlaceholders = false,
                initialLoadSize = QUERY_LIMIT * 3,
                maxSize = 50
            ),
            1
        ) {
            QueryBookPagingSource(booksRepository, query).also { prevSource = it }
        }.flow.map {
            mapPagingData(it)
        }.cachedIn(viewModelScope)
        return pager
    }

    private suspend fun mapPagingData(data: PagingData<Book>): PagingData<UIBook> {
        return data.map { book ->
            Log.d("MainViewModel", "mapPagingData: data is being mapped...")
            book.isLiked = favoritesRepository.getBook(book.title)?.isLiked ?: false
            UIBook(
                book.title,
                book.author,
                book.coverI,
                {
                    Log.d("MainViewModel", "mapResponseToUI: clicked")
                    addToFavorites(book)
                },
                {
                    Log.d("MainViewModel", "mapResponseToUI: long clicked")
                    showDetailedInfo(book)
                },
                book::isLiked
            )
        }
    }

    private fun showDetailedInfo(book: Book) {

    }

    private fun addToFavorites(book: Book) {
        viewModelScope.launch {
            book.isLiked = !book.isLiked
            if (book.isLiked)
                favoritesRepository.insert(book)
            else
                favoritesRepository.delete(book)
        }
    }

//    class Factory(private val booksRepository: BooksRepository) : ViewModelProvider.Factory{
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            return MainViewModel(booksRepository) as T
//        }
//    }
}