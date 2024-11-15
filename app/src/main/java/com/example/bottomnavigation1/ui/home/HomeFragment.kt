package com.example.bottomnavigation1.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bottomnavigation1.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var savedText: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.toggleButton.setOnClickListener {
            showInputDialog()
        }

        return root
    }

    private fun showInputDialog() {
        val dialog = InputDialogHomeFragment()
        val args = Bundle()
        args.putString("savedText", savedText)
        dialog.arguments = args
        dialog.show(parentFragmentManager, "InputDialogFragment")
    }

    fun onInputDialogSave(inputText: String) {
        savedText = inputText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}