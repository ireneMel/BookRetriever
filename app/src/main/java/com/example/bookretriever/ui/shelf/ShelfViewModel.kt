package com.example.bookretriever.ui.shelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookretriever.models.Book
import com.example.bookretriever.repositories.ShelfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShelfViewModel @Inject constructor(
    private val favoritesRepository: ShelfRepository
) : ViewModel() {

    private val _uiBookList = MutableStateFlow(emptyList<Book>())
    val uiBookList = _uiBookList.asStateFlow()

    init {
        fetchBooks()
    }

    private fun fetchBooks() =
        viewModelScope.launch(Dispatchers.IO) {
            favoritesRepository.getAllBooks().onEach { _uiBookList.value = it }.collect()
        }
}