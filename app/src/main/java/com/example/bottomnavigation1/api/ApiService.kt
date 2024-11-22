package com.example.bottomnavigation1.api


import com.example.bottomnavigation1.model.GenerateRequest
import com.example.bottomnavigation1.model.LoginRequest
import com.example.bottomnavigation1.model.RegisterRequest
import com.example.bottomnavigation1.model.UpdateProfileRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiService {
    @POST("/generate")
    @Headers("Content-Type: application/json")
    fun generateImage(@Body request: GenerateRequest): Call<ResponseBody>

    @Multipart
    @POST("imgToImg")  // 后端的接口地址
    fun imgToImg(
        @Part image: MultipartBody.Part,  // 图片部分
        @Part("positivePrompt") positivePrompt: RequestBody,
        @Part("negativePrompt") negativePrompt: RequestBody,
        @Part("guidanceScale") guidanceScale: RequestBody,
        @Part("numInferenceSteps") numInferenceSteps: RequestBody,
        @Part("seed") seed: RequestBody?  // 可选
    ): Call<ResponseBody>

    @POST("/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<Map<String, String>>

    @POST("/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<Map<String, String>>

    @GET("/get_user_by_name")
    suspend fun getUserByName(@Query("name") name: String): Response<Map<String, Any>>


    @GET("/get_user_by_email")
    suspend fun getUserByEmail(@Query("email") email: String): Response<Map<String, Any>>


    @PUT("/update_password")
    suspend fun updatePassword(@Body updatePasswordRequest: Map<String, String>): Response<Map<String, String>>


    @PUT("/update_profile")
    suspend fun updateProfile(@Body updateProfileRequest: UpdateProfileRequest): Response<Map<String, String>>
}