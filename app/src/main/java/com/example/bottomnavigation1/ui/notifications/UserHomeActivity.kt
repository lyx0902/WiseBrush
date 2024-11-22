package com.example.bottomnavigation1.ui.notifications

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavigation1.R

class UserHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        val username = intent.getStringExtra("username")
        Log.d("UserHomeActivity", "Username: $username")

        val usernameTextView = findViewById<TextView>(R.id.usernameTextView)
        if (username != null) {
            usernameTextView.text = username
        } else {
            usernameTextView.text = "Unknown User"
        }

        val updateOptionsSpinner = findViewById<Spinner>(R.id.updateOptionsSpinner)
        val updateValueEditText = findViewById<EditText>(R.id.updateValueEditText)
        val updateButton = findViewById<Button>(R.id.updateButton)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        updateButton.setOnClickListener {
            val selectedOption = updateOptionsSpinner.selectedItem.toString()
            val newValue = updateValueEditText.text.toString()

            when (selectedOption) {
                "Update Username" -> {
                    // Handle username update
                    Toast.makeText(this, "Username updated to $newValue", Toast.LENGTH_SHORT).show()
                }
                "Update Password" -> {
                    // Handle password update
                    Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show()
                }
                "Update Email" -> {
                    // Handle email update
                    Toast.makeText(this, "Email updated to $newValue", Toast.LENGTH_SHORT).show()
                }
            }
        }

        logoutButton.setOnClickListener {
            finish() // Close the UserHomeActivity and return to NotificationsFragment
        }
    }
}