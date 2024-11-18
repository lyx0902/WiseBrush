package com.example.bottomnavigation1.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.identity.util.UUID

data class User (
    val id: String,
    val name: String,
    val password: String,
    val email: String
)