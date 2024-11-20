package com.example.bottomnavigation1.utils

object encryptPsw {
    fun encryptPassword(plainPassword: String): String {
        return org.mindrot.jbcrypt.BCrypt.hashpw(plainPassword, org.mindrot.jbcrypt.BCrypt.gensalt())
    }
    fun checkPassword(plainPassword: String, hashedPassword: String): Boolean {
        return org.mindrot.jbcrypt.BCrypt.checkpw(plainPassword, hashedPassword)
    }
}