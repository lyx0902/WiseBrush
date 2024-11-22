package com.example.bottomnavigation1.repository

import com.example.bottomnavigation1.api.RetrofitInstance
import com.example.bottomnavigation1.database.Database
import com.example.bottomnavigation1.model.LoginRequest
import com.example.bottomnavigation1.model.RegisterRequest
import com.example.bottomnavigation1.model.UpdateProfileRequest
import com.example.bottomnavigation1.model.User
import com.example.bottomnavigation1.utils.encryptPsw.encryptPassword

import java.sql.SQLException

object UserRepository {
    suspend fun registerUser(name: String, password: String, email: String): Result<String> {
        val registerRequest = RegisterRequest(name, password, email)
        return try {
            val response = RetrofitInstance.apiService.registerUser(registerRequest)
            if (response.isSuccessful) {
                Result.success("注册成功")
            } else {
                Result.failure(Exception("注册失败: ${response.body()?.get("message") ?: "未知错误"}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("注册失败: ${e.message}"))
        }
    }

    // 用户登录
    suspend fun loginUser(name: String, password: String): Result<String> {
        val loginRequest = LoginRequest(name, password)
        return try {
            val response = RetrofitInstance.apiService.loginUser(loginRequest)
            if (response.isSuccessful) {
                val message = response.body()?.get("message") ?: "登录成功"
                Result.success(message)
            } else {
                Result.failure(Exception("登录失败: ${response.body()?.get("message") ?: "未知错误"}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("登录失败: ${e.message}"))
        }
    }

    // 根据用户名查询用户
    suspend fun getUserByName(name: String): Result<Map<String, Any>> {
        return try {
            val response = RetrofitInstance.apiService.getUserByName(name)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyMap())
            } else {
                Result.failure(Exception("查询失败: ${response.body()?.get("message") ?: "未知错误"}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("查询失败: ${e.message}"))
        }
    }

    // 根据邮箱查询用户
    suspend fun getUserByEmail(email: String): Result<Map<String, Any>> {
        return try {
            val response = RetrofitInstance.apiService.getUserByEmail(email)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyMap())
            } else {
                Result.failure(Exception("查询失败: ${response.body()?.get("message") ?: "未知错误"}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("查询失败: ${e.message}"))
        }
    }

    // 修改密码
    suspend fun updatePassword(name: String, oldPassword: String, newPassword: String): Result<String> {
        val updatePasswordRequest = mapOf("name" to name, "old_password" to oldPassword, "new_password" to newPassword)
        return try {
            val response = RetrofitInstance.apiService.updatePassword(updatePasswordRequest)
            if (response.isSuccessful) {
                Result.success("密码修改成功")
            } else {
                Result.failure(Exception("密码修改失败: ${response.body()?.get("message") ?: "未知错误"}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("密码修改失败: ${e.message}"))
        }
    }

    // 修改个人信息
    suspend fun updateProfile(name: String, newName: String,newPassword: String, newEmail: String): Result<String> {
        val updateProfileRequest = UpdateProfileRequest(name, newName,newPassword, newEmail)
        return try {
            val response = RetrofitInstance.apiService.updateProfile(updateProfileRequest)
            if (response.isSuccessful) {
                Result.success("个人信息修改成功")
            } else {
                Result.failure(Exception("个人信息修改失败: ${response.body()?.get("message") ?: "未知错误"}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("个人信息修改失败: ${e.message}"))
        }
    }

}