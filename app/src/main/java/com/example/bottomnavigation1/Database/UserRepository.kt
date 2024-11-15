package com.example.bottomnavigation1.Database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }
}