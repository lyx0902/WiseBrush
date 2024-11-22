package com.example.bottomnavigation1.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import android.widget.Toast
import android.widget.ViewSwitcher
import com.example.bottomnavigation1.R
import com.example.bottomnavigation1.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotificationsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            viewModel.login(username, password)
        }

        binding.registerButton.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            val email = binding.email.text.toString()
            viewModel.register(username, password, email)
        }

        viewModel.loginResult.observe(viewLifecycleOwner, Observer { result ->
            if (result =="登录成功") {
                val username = binding.username.text.toString()
                if (username.isNotEmpty()) {
                    val intent = Intent(requireContext(), UserHomeActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "Username cannot be empty", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.registerResult.observe(viewLifecycleOwner, Observer { result ->
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}