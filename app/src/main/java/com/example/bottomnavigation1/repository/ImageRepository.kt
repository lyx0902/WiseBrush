package com.example.bottomnavigation1.repository


import android.content.Context
import com.example.bottomnavigation1.api.RetrofitInstance
import com.example.bottomnavigation1.model.GenerateRequest

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

// to do
class ImageRepository {

    // 请求生成图像并保存
    fun generateImageAndSave(context: Context, prompt: String, callback: (Result<File>) -> Unit) {
        val request = GenerateRequest(prompt)

        RetrofitInstance.apiService.generateImage(request).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        // 获取图像字节流
                        val inputStream = responseBody.byteStream()
                        try {
                            // 保存图像到文件
                            val file = saveImageToFile(context, inputStream)
                            callback(Result.success(file)) // 返回保存的文件
                        } catch (e: IOException) {
                            callback(Result.failure(e)) // 如果保存失败，返回失败结果
                        }
                    } ?: run {
                        callback(Result.failure(Exception("Response body is null")))
                    }
                } else {
                    callback(Result.failure(Exception("Error: ${response.code()}"))) // 错误处理
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback(Result.failure(t)) // 返回请求失败的异常
            }
        })
    }

    // 保存图像到本地文件
    private fun saveImageToFile(context: Context, inputStream: java.io.InputStream): File {
        val file = File(context.filesDir, "generated_image.png")
        val outputStream = FileOutputStream(file)

        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output) // 将输入流复制到文件输出流
            }
        }

        return file // 返回保存的文件
    }
}