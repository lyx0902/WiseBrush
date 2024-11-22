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
            try {
                // 获取结果
                val result = UserRepository.getUserByName(username)

                result.onSuccess { data ->
                    // 确保 data 是非空的 Map 类型
                    if (data is Map<*, *>) {
                        // 从 data 中获取用户信息
                        println("Data: $data")
                        println("ID: ${data["id"]}, Name: ${data["name"]}, Password: ${data["password"]}, Email: ${data["email"]}")

                        val id = (data["id"] as? Int) ?: -1
                        val name = (data["name"] as? String) ?: "Unknown"
                        val password = (data["password"] as? String) ?: ""
                        val email = (data["email"] as? String) ?: ""

                        // 创建 User 实例并更新 LiveData
                        _userProfile.value = User(id, name, password, email)
                    } else {
                        // data 类型不匹配，设置默认用户
                        _userProfile.value = User(
                            id = -1,
                            name = "Invalid Data",
                            password = "",
                            email = ""
                        )
                    }
                }.onFailure {
                    // 请求失败时设置默认用户
                    _userProfile.value = User(
                        id = -1,
                        name = "Unknown User",
                        password = "",
                        email = ""
                    )
                }
            } catch (e: Exception) {
                // 捕获异常，避免闪退
                _userProfile.value = User(
                    id = -1,
                    name = "Error Occurred",
                    password = "",
                    email = ""
                )
                e.printStackTrace() // 打印异常日志便于调试
            }
        }
    }
//
    fun updateUserProfile(username: String, newUsername: String, newPassword: String, newEmail: String) {
        viewModelScope.launch {
            UserRepository.updateProfile(username, newUsername, newEmail)
        }
    }
}