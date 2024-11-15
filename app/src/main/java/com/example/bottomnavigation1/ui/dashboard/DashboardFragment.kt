package com.example.bottomnavigation1.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bottomnavigation1.databinding.FragmentDashboardBinding
import com.example.bottomnavigation1.ui.home.InputDialogHomeFragment

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var drawingView: DrawingView
    private var savedText: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        drawingView = DrawingView(requireContext(), null)
        binding.drawingFrame.addView(drawingView)

        binding.toggleButton.setOnClickListener {
            showInputDialog()
        }

        binding.brushButton.setOnClickListener {
            drawingView.setErase(false)
            binding.colorPalette.visibility = View.VISIBLE
        }

        binding.eraserButton.setOnClickListener {
            drawingView.setErase(true)
            binding.colorPalette.visibility = View.GONE
        }

        binding.clearButton.setOnClickListener {
            drawingView.startNew()
            binding.colorPalette.visibility = View.GONE
        }

        binding.colorButton1.setOnClickListener {
            drawingView.setColor(Color.RED)
            binding.colorPalette.visibility = View.GONE
        }

        binding.colorButton2.setOnClickListener {
            drawingView.setColor(Color.GREEN)
            binding.colorPalette.visibility = View.GONE
        }

        binding.colorButton3.setOnClickListener {
            drawingView.setColor(Color.BLUE)
            binding.colorPalette.visibility = View.GONE
        }

        binding.colorButton4.setOnClickListener {
            drawingView.setColor(Color.YELLOW)
            binding.colorPalette.visibility = View.GONE
        }

        root.setOnClickListener {
            binding.colorPalette.visibility = View.GONE
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}