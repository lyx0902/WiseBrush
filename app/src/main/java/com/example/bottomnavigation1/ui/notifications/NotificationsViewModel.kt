package com.example.bottomnavigation1.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NotificationsViewModel : ViewModel() {

    fun login(username: String, password: String) {
        viewModelScope.launch {
            // Implement login logic here
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            // Implement registration logic here
        }
    }
}