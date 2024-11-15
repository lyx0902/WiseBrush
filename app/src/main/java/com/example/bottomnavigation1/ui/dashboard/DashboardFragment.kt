package com.example.bottomnavigation1.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.bottomnavigation1.databinding.FragmentDashboardBinding
import com.example.bottomnavigation1.ui.home.InputDialogHomeFragment

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var drawingView: DrawingView
    private var savedText: String? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val imageUri: Uri? = data?.data
            if (imageUri != null) {
                loadImageFromUri(imageUri)
            }
        }
    }

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

        binding.uploadButton.setOnClickListener {
            openGallery()
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

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImage.launch(intent)
    }

    private fun loadImageFromUri(imageUri: Uri) {
        val inputStream = requireContext().contentResolver.openInputStream(imageUri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        originalBitmap?.let {
            val viewWidth = drawingView.width
            val viewHeight = drawingView.height

            val scaledBitmap = Bitmap.createScaledBitmap(it, viewWidth, viewHeight, true)
            drawingView.setImageBitmap(scaledBitmap)
        }
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