package com.example.bottomnavigation1.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "http://172.172.52.124:5000"
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // 记录完整的请求和响应体
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        //to do
        .connectTimeout(60, TimeUnit.SECONDS)  // 设置连接超时
        .readTimeout(60, TimeUnit.SECONDS)     // 设置读取超时
        .writeTimeout(60, TimeUnit.SECONDS)    // 设置写入超时
        .build()
    private val retrofit by lazy {
        Retrofit.Builder()

            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}