package com.example.bottomnavigation1.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bottomnavigation1.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private var savedText: String? = null
    private lateinit var drawingView: DrawingView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.toggleButton.setOnClickListener {
            showInputDialog()
        }

        drawingView = DrawingView(requireContext(), null)
        binding.drawingFrame.addView(drawingView)

        binding.brushButton.setOnClickListener {
            drawingView.setErase(false)
            drawingView.setBrushSize(10f)
        }

        binding.eraserButton.setOnClickListener {
            drawingView.setErase(true)
            drawingView.setBrushSize(20f)
        }

        binding.clearButton.setOnClickListener {
            drawingView.startNew()
        }

        return root
    }

    private fun showInputDialog() {
        val dialog = InputDialogDashboardFragment()
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