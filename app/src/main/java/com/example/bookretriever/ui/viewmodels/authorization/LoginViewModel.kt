package com.example.bookretriever.ui.viewmodels.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookretriever.repositories.UserState
import com.example.bookretriever.repositories.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

sealed class LoginUserState {
    object Unknown : LoginUserState()
    object Verified : LoginUserState()
    object Loading : LoginUserState()
}

class LoginViewModel : ViewModel() {
    private val repository = UsersRepository()

    private val _state = MutableStateFlow<LoginUserState>(LoginUserState.Unknown)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.userState.onEach {
                if (it == UserState.Unknown || it == UserState.NotVerified)
                    _state.value = LoginUserState.Unknown
                else if (it == UserState.Verified)
                    _state.value = LoginUserState.Verified
            }.collect()
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginUserState.Loading
            repository.logInUser(email, password) //TODO error handling
        }
    }

    fun resetPassword(resetMail: String): Boolean = repository.resetPassword(resetMail)

}