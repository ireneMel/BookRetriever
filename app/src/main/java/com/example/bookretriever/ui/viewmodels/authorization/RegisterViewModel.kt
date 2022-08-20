package com.example.bookretriever.ui.viewmodels.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookretriever.repositories.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val repository = UsersRepository()

    private val _isComplete = MutableStateFlow(false)
    val isComplete = _isComplete.asStateFlow()

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isComplete.value = repository.registerUser(name, email, password)
        }
    }
}