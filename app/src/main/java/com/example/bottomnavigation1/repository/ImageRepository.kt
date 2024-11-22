package com.example.bottomnavigation1.repository


import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.bottomnavigation1.api.RetrofitInstance
import com.example.bottomnavigation1.model.GenerateRequest

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

// to do
object ImageRepository {


    fun generateImageAndSave(context: Context, request: GenerateRequest, callback: (Result<File>) -> Unit) {
        RetrofitInstance.apiService.generateImage(request).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        // 获取图像字节流
                        val inputStream = responseBody.byteStream()
                        try {
                            // 保存图像到图库指定文件夹
                            // to do此处应该改为用户（如有
                            val fileUri = saveImageToGallery(context, inputStream, request.positivePrompt)
                            if (fileUri != null) {
                                callback(Result.success(File(fileUri.path))) // 返回文件 URI
                            } else {
                                callback(Result.failure(Exception("Failed to save image to gallery")))
                            }
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
                // 打印请求 URL 和头部信息
                Log.e("Request", "Request URL: ${call.request().url}")
                Log.e("Request", "Request Headers: ${call.request().headers}")
            }
        })
    }

    // 保存图像到图库特定文件夹
    private fun saveImageToGallery(context: Context, inputStream: InputStream, imageName: String): Uri? {
        val contentResolver = context.contentResolver

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$imageName.png")  // 图片名
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")          // 图片格式
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/YourAppFolder") // 图片文件夹路径（你的应用文件夹）
            put(MediaStore.Images.Media.IS_PENDING, 1)                    // 文件处于待处理状态
        }

        // 插入文件到 MediaStore
        val imageUri: Uri? = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        imageUri?.let { uri ->
            try {
                // 打开输出流并将图片数据写入
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    inputStream.copyTo(outputStream)  // 将输入流复制到输出流
                }

                // 更新文件的 IS_PENDING 状态，标记为已保存
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(uri, contentValues, null, null)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return imageUri  // 返回保存后的 Uri
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