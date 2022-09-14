package com.example.bookretriever.ui.authorization.register

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookretriever.repositories.UsersRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed class RegisterErrorEvent {
    object ExistingUserMessage : RegisterErrorEvent()
}

class RegisterViewModel : ViewModel() {
    private val repository = UsersRepository()

    private val _isComplete = MutableStateFlow(false)
    val isComplete = _isComplete.asStateFlow()

    private val registerErrorEventChannel = Channel<RegisterErrorEvent>()
    val errorEventFlow = registerErrorEventChannel.receiveAsFlow()

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isComplete.value = repository.registerUser(name, email, password)
            if (!_isComplete.value) {
                registerErrorEventChannel.send(RegisterErrorEvent.ExistingUserMessage)
            }
        }
    }


}