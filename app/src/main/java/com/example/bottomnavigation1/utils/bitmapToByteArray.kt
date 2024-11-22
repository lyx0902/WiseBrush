package com.example.bottomnavigation1.utils

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

object bitmapToByteArray {
    fun getBitmapAsByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream) // 使用 PNG 格式或其他适合的格式
        return stream.toByteArray()
    }
}