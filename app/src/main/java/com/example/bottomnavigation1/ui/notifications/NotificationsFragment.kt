package com.example.bottomnavigation1.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.bottomnavigation1.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotificationsViewModel by viewModels()
    private var currentToast: Toast? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.clearMessages()
        currentToast?.cancel()
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
            currentToast?.cancel()
            if (!result.isNullOrEmpty()) {
                currentToast = Toast.makeText(context, result, Toast.LENGTH_SHORT)
                currentToast?.show()
            }
        })

        viewModel.registerResult.observe(viewLifecycleOwner, Observer { result ->
            currentToast?.cancel()
            if (!result.isNullOrEmpty()) {
                currentToast = Toast.makeText(context, result, Toast.LENGTH_SHORT)
                currentToast?.show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}