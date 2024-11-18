package com.example.bottomnavigation1

import UserRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bottomnavigation1.model.User
import com.example.bottomnavigation1.model.UserResponse

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()

    val usersLiveData: LiveData<List<User>> = userRepository.usersLiveData
    val userByIdLiveData: LiveData<User?> = userRepository.userByIdLiveData

    // 获取所有用户数据
    fun getAllUsers() : List<UserResponse>{
        userRepository.getAllUsers()
        return emptyList()//
    }

    // 根据ID获取单个用户数据
    fun getUserById(id: Int) {
        userRepository.getUserById(id)
    }

    // 插入新用户数据
    fun insertUser(user: User) {
        userRepository.insertUser(user)
    }

    // 更新用户数据（示例：根据ID更新用户邮箱）
    fun updateUser(id: Int, user: User) {
        userRepository.updateUser(id, user)
    }

    // 删除用户数据（示例：根据ID删除用户）
    fun deleteUser(id: Int) {
        userRepository.deleteUser(id)
    }
}