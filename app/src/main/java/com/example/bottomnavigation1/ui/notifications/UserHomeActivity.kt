package com.example.bottomnavigation1.ui.notifications

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavigation1.R

class UserHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        val username = intent.getStringExtra("username")
        Log.d("UserHomeActivity", "Username: $username") // 添加日志记录

        val usernameTextView = findViewById<TextView>(R.id.usernameTextView)
        if (username != null) {
            usernameTextView.text = username
        } else {
            usernameTextView.text = "Unknown User"
        }

        val logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            finish() // Close the UserHomeActivity and return to NotificationsFragment
        }
    }
}