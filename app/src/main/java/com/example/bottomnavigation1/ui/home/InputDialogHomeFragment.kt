package com.example.bottomnavigation1.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.bottomnavigation1.R
import com.example.bottomnavigation1.model.User
import com.example.bottomnavigation1.repository.UserRepository

class InputDialogHomeFragment : DialogFragment() {

    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView
    private lateinit var editText1: EditText
    private lateinit var editText2: EditText
    private lateinit var editText3: EditText
    private lateinit var textPrompt1: EditText
    private lateinit var textPrompt2: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_input_dialog, container, false)

        val viewPrompt=inflater.inflate(R.layout.fragment_home, container, false)
        textView1 = view.findViewById(R.id.textView1)
        textView2 = view.findViewById(R.id.textView2)
        textView3 = view.findViewById(R.id.textView3)
        editText1 = view.findViewById(R.id.editText1)
        editText2 = view.findViewById(R.id.editText2)
        editText3 = view.findViewById(R.id.editText3)
        saveButton = view.findViewById(R.id.saveButton)
        cancelButton = view.findViewById(R.id.cancelButton)

        textPrompt1=viewPrompt.findViewById<EditText?>(R.id.editText1)//正提示词
        textPrompt2=viewPrompt.findViewById(R.id.editText2)//反提示词



        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("InputDialogPrefs", Context.MODE_PRIVATE)


        // Load previously saved text or set default value
        val savedText1 = sharedPreferences.getString("savedText1", "Please input")
        val savedText2 = sharedPreferences.getString("savedText2", "Please input")
        val savedText3 = sharedPreferences.getString("savedText3", "Please input")
        textView1.text = "相关性"
        textView2.text = "步数"
        textView3.text = "可选的种子(默认为null)"
        editText1.setText(savedText1)
        editText2.setText(savedText2)
        editText3.setText(savedText3)

        // Save button click listener
        saveButton.setOnClickListener {
            val textToSave1 = editText1.text.toString()
            val textToSave2 = editText2.text.toString()
            val textToSave3 = editText3.text.toString()
            saveText(textToSave1, textToSave2, textToSave3, textPrompt1.toString(),
                textPrompt2.toString()
            )
            dismiss() // Close the dialog
        }

        // Cancel button click listener
        cancelButton.setOnClickListener {
            dismiss() // Close the dialog without saving
        }

        return view
    }

    private fun saveText(text1: String, text2: String, text3: String, text4: String, text5: String) {
        with(sharedPreferences.edit()) {
            UserRepository.addUser(User(id  = null, name = "John Doe", password = "123456", email = "123"));

            putString("savedText1", text1)
            putString("savedText2", text2)
            putString("savedText3", text3)
            putString("positivePrompt", text4)
            putString("negativePrompt", text5)
            apply()
        }
    }
}