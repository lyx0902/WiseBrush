package com.example.bottomnavigation1.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bottomnavigation1.repository.UserRepository
import kotlinx.coroutines.launch

class NotificationsViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<String>()
    val loginResult: LiveData<String> get() = _loginResult

    private val _registerResult = MutableLiveData<String>()
    val registerResult: LiveData<String> get() = _registerResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = UserRepository.loginUser(username, password)
            result.onSuccess {
                _loginResult.value = it
            }.onFailure {
                _loginResult.value = it.message
            }
        }
    }

    fun register(username: String, password: String, email: String) {
        viewModelScope.launch {
            val result = UserRepository.registerUser(username, password, email)
            result.onSuccess {
                _registerResult.value = it
            }.onFailure {
                _registerResult.value = it.message
            }
        }
    }
}