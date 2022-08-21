package com.example.bookretriever.ui.viewmodels.authorization

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
    private val TAG = "Login ViewModel"
    private val repository = UsersRepository()

    private val loginErrorEventChannel = Channel<LoginErrorEvent>()
    val errorEventFlow = loginErrorEventChannel.receiveAsFlow()

    private val _state = MutableStateFlow<LoginUserState>(LoginUserState.Unknown)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.userState.onEach {
                when (it) {
                    UserState.Unknown, UserState.NotVerified -> _state.value =
                        LoginUserState.Unknown
                    UserState.Verified -> _state.value = LoginUserState.Verified
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
            Log.d(TAG, "login: logged in")
        }
    }

    fun resetPassword(resetMail: String): Boolean = repository.resetPassword(resetMail)

}