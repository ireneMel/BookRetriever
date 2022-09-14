package com.example.bookretriever.ui.authorization.login

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookretriever.repositories.UserState
import com.example.bookretriever.repositories.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class LoginUserState {
    object Unknown : LoginUserState()
    object Verified : LoginUserState()
    object Loading : LoginUserState()
    object Error : LoginUserState()
}

sealed class LoginErrorEvent {
    data class ErrorMessage(val message: String) : LoginErrorEvent()
}

class LoginViewModel : ViewModel() {
    private val repository = UsersRepository()

    private val loginErrorEventChannel = Channel<LoginErrorEvent>()
    val errorEventFlow = loginErrorEventChannel.receiveAsFlow()

    private val _state =
        if (repository.isUserVerified) MutableStateFlow<LoginUserState>(LoginUserState.Verified)
        else MutableStateFlow<LoginUserState>(LoginUserState.Unknown)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.userState.onEach {
                when (it) {
                    UserState.Verified -> _state.value = LoginUserState.Verified
                    UserState.Unknown -> _state.value = LoginUserState.Unknown
                    UserState.NotVerified -> {
                        _state.value = LoginUserState.Unknown
                        loginErrorEventChannel.send(LoginErrorEvent.ErrorMessage("The email has not been verified"))
                    }
                    is UserState.Error -> {
                        _state.value = LoginUserState.Error
                        loginErrorEventChannel.send(LoginErrorEvent.ErrorMessage(it.message))
                    }
                }
            }.collect()
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginUserState.Loading
            repository.logInUser(email, password)
        }
    }

    fun resetPassword(resetMail: String): Boolean = repository.resetPassword(resetMail)
}