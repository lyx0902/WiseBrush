package com.example.bottomnavigation1

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bottomnavigation1.databinding.ActivityMainBinding
//import com.example.bottomnavigation1.Database.User
//import com.example.bottomnavigation1.Database.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
//    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.drawingViewContainer.visibility = View.GONE
        }
//
//        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
//
//        // Insert sample user data
//        val users = listOf(
//            User(name = "John Doe", email = "john.doe@example.com"),
//            User(name = "Jane Smith", email = "jane.smith@example.com"),
//            User(name = "Alice Johnson", email = "alice.johnson@example.com")
//        )
//
//        users.forEach { user ->
//            userViewModel.insert(user)
//        }
    }
}