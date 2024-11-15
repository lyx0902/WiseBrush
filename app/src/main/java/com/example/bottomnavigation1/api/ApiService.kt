package com.example.bottomnavigation1.api


import com.example.bottomnavigation1.model.GenerateRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface ApiService {
    @POST("/generate")
    @Headers("Content-Type: application/json")
    fun generateImage(@Body request: GenerateRequest): Call<ResponseBody>

}