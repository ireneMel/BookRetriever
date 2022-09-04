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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class MainViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
    private val favoritesRepository: ShelfRepository
) : ViewModel() {

//    private val _uiBookList = MutableStateFlow(emptyList<UIBook>())
//    val uiBookList = _uiBookList.asStateFlow()
    private val queryFlow = MutableStateFlow<String?>(null)
    private var prevSource: PagingSource<*,*>? = null
    val bookFlow = queryFlow.flatMapLatest {
//        getBooksByQuery("harry")
        prevSource?.invalidate()
        if (it == null) getTrendingBooks() else getBooksByQuery(it)
    }

    fun querySubmitted(q: String) {
        queryFlow.value = q
    }

    private fun getTrendingBooks(): Flow<PagingData<UIBook>> {
        return Pager(
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
    }

    private fun getBooksByQuery(query: String): Flow<PagingData<UIBook>> {
        println(query)
        return Pager(
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

    private suspend fun mapResponseToUI(response: List<Book>) =
        response.map { book ->
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

//    fun fetchBooksByName(query: String) {
//        viewModelScope.launch {
//            val response = client.getBookByTitle(query, "everything")
//            if (response.isSuccessful) {
//                val bookResponse: BookResponse = response.body() ?: return@launch
//                list.value = bookResponse.works.map {
//                    BookEntity(
//                        it?.isbn,
//                        it?.title,
//                        it.authorName?.get(0),
//                        it?.cover_i
//                    )
//                }
//            }
//        }
//    }

//    class Factory(private val booksRepository: BooksRepository) : ViewModelProvider.Factory{
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            return MainViewModel(booksRepository) as T
//        }
//    }
}