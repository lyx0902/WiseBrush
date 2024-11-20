package com.example.bottomnavigation1.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object Database {
    private const val URL = "jdbc:mysql://172.172.52.124:3306/draw"
    private const val USER = "root"
    private const val PASSWORD = "AIdrawingzyc123...sdahb"

    fun getConnection(): Connection? {
        return try {
            DriverManager.getConnection(URL, USER, PASSWORD)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }
}