package com.example.bottomnavigation1.api
import com.example.bottomnavigation1.model.User
import retrofit2.Call
import retrofit2.http.*

interface UserApi {
    // 获取所有用户数据
    @GET("users")
    fun getUsers(): Call<List<User>>

    // 根据ID获取单个用户数据
    @GET("users/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>

    // 插入新用户数据
    @POST("users")
    fun insertUser(@Body user: User): Call<User>

    // 更新用户数据（示例：根据ID更新用户邮箱）
    @PUT("users/{id}")
    fun updateUser(@Path("id") id: Int, @Body user: User): Call<User>

    // 删除用户数据（示例：根据ID删除用户）
    @DELETE("users/{id}")
    fun deleteUser(@Path("id") id: Int): Call<Void>
}