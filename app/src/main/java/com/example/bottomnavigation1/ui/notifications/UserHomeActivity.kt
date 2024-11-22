package com.example.bottomnavigation1.ui.notifications

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.bottomnavigation1.R

class UserHomeActivity : AppCompatActivity() {

    private lateinit var viewModel: NotificationsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        viewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        val username = intent.getStringExtra("username")
        Log.d("UserHomeActivity", "Username: $username")

        val usernameTextView = findViewById<TextView>(R.id.usernameTextView)
        val updateUsernameEditText = findViewById<EditText>(R.id.updateUsernameEditText)
        val updatePasswordEditText = findViewById<EditText>(R.id.updatePasswordEditText)
        val updateEmailEditText = findViewById<EditText>(R.id.updateEmailEditText)
        val updateButton = findViewById<Button>(R.id.updateButton)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        if (username != null) {
            usernameTextView.text = username
            viewModel.getUserProfile(username)
            viewModel.userProfile.observe(this, { profile ->
                updateUsernameEditText.setText(profile.name)
                updatePasswordEditText.setText(profile.password)
                updateEmailEditText.setText(profile.email)
            })
        } else {
            usernameTextView.text = "Unknown User"
        }

        updateButton.setOnClickListener {
            val newUsername = updateUsernameEditText.text.toString()
            val newPassword = updatePasswordEditText.text.toString()
            val newEmail = updateEmailEditText.text.toString()

            viewModel.updateUserProfile(username!!, newUsername, newPassword, newEmail)
            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
        }

        logoutButton.setOnClickListener {
            finish() // Close the UserHomeActivity and return to NotificationsFragment
        }
    }
}