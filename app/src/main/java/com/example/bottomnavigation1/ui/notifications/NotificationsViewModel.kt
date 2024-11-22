package com.example.bottomnavigation1.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bottomnavigation1.model.User
import com.example.bottomnavigation1.repository.UserRepository
import kotlinx.coroutines.launch

class NotificationsViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<String>()
    val loginResult: LiveData<String> get() = _loginResult

    private val _registerResult = MutableLiveData<String>()
    val registerResult: LiveData<String> get() = _registerResult

    private val _userProfile = MutableLiveData<User>()
    val userProfile: LiveData<User> get() = _userProfile

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

    fun getUserProfile(username: String) {
        viewModelScope.launch {
            val result = UserRepository.getUserByName(username)

            result.onSuccess { data ->
                val user = User(
                    id = data["id"] as Int,
                    name = data["name"] as String,
                    password = data["password"] as String,
                    email = data["email"] as String
                )
                _userProfile.value = user
            }.onFailure {
                _userProfile.value = User(
                    id = -1,
                    name = "Unknown User",
                    password = "",
                    email = ""
                )
            }
        }
    }

    fun updateUserProfile(username: String, newUsername: String, newPassword: String, newEmail: String) {
        viewModelScope.launch {
            UserRepository.updateProfile(username, newUsername, newEmail)
        }
    }
}