package com.example.bookretriever.ui.trending

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookretriever.models.Book
import com.example.bookretriever.models.UIBook
import com.example.bookretriever.repositories.BooksRepository
import com.example.bookretriever.repositories.ShelfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Boolean
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
    private val favoritesRepository: ShelfRepository
) : ViewModel() {

    private val _uiBookList = MutableStateFlow(emptyList<UIBook>())
    val uiBookList = _uiBookList.asStateFlow()

    init {
        fetchBooks()
    }

    private fun fetchBooks() =
        viewModelScope.launch(Dispatchers.IO) {
            val response = booksRepository.getTrendingBooks()
            if (response?.response != null)
                _uiBookList.value = mapResponseToUI(response.response)
//            else //error
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