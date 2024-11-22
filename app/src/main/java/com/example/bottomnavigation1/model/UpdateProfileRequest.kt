package com.example.bottomnavigation1.model

data class UpdateProfileRequest(
    val name: String,
    val new_name : String,
    val new_password :String,
    val new_email : String
)
