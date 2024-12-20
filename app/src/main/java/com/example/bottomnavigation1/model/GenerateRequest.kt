package com.example.bottomnavigation1.model



data class GenerateRequest(
    val positivePrompt: String,
    val negativePrompt: String,
    val guidanceScale: Double = 7.5, //相关性
    val numInferenceSteps: Int = 50, //步数
    val seed: Int? = null   // 可选的种子，默认为 null
)
