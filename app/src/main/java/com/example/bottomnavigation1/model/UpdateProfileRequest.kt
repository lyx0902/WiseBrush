package com.example.bottomnavigation1.model

data class UpdateProfileRequest(
    val name: String,
    val newName : String,
    val newPassword :String,
    val newEmail : String
)
