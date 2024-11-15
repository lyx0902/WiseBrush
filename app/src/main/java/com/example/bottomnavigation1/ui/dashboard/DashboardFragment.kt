package com.example.bottomnavigation1.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bottomnavigation1.databinding.FragmentDashboardBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val editText1: EditText = binding.editText1
        val editText2: EditText = binding.editText2
        val submitButton: Button = binding.submitButton

        submitButton.setOnClickListener {
            val text1 = editText1.text.toString()
            val text2 = editText2.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    uploadData(text1, text2)
                } catch (e: Exception) {
                    Log.e("DashboardFragment", "Error uploading data", e)
                }
            }
        }

        return root
    }

    private fun uploadData(text1: String, text2: String) {
        val client = OkHttpClient()
        val url = "https://yourserver.com/upload"
        val json = """{"text1":"$text1","text2":"$text2"}"""
        val body = json.toRequestBody()

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        //
        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                Log.d("DashboardFragment", "Data uploaded successfully")
            } else {
                Log.e("DashboardFragment", "Failed to upload data: ${response.message}")
            }
        } catch (e: IOException) {
            Log.e("DashboardFragment", "Network error", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}