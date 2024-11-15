package com.example.bottomnavigation1.ui.dashboard

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.bottomnavigation1.databinding.FragmentDashboardBinding
import com.example.bottomnavigation1.ui.home.InputDialogHomeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

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

    private val pickImage111 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val imageUri: Uri? = data?.data
            if (imageUri != null) {
                loadImageFromUri111(imageUri)
            }
        }
    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_WRITE_STORAGE)
        }
    }

    companion object {
        private const val REQUEST_WRITE_STORAGE = 112
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requestStoragePermission()

        drawingView = DrawingView(requireContext(), null)
        binding.drawingFrame.addView(drawingView)

        binding.toggleButton.setOnClickListener {
            showInputDialog()
        }

        binding.uploadButton.setOnClickListener {
            openGallery()
        }

        binding.downloadButton.setOnClickListener {
            showDownloadDialog()
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

    private fun openGallery111() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImage111.launch(intent)
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

    private fun loadImageFromUri111(imageUri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                val originalBitmap = BitmapFactory.decodeStream(inputStream)
                val maxDimension = 512
                val aspectRatio = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()
                val scaledWidth: Int
                val scaledHeight: Int

                if (originalBitmap.width > originalBitmap.height) {
                    scaledWidth = maxDimension
                    scaledHeight = (maxDimension / aspectRatio).toInt()
                } else {
                    scaledHeight = maxDimension
                    scaledWidth = (maxDimension * aspectRatio).toInt()
                }

                val scaledBitmap = Bitmap.createScaledBitmap(
                    originalBitmap,
                    scaledWidth,
                    scaledHeight,
                    true
                )
                withContext(Dispatchers.Main) {
                    binding.imageView.setImageBitmap(scaledBitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showInputDialog() {
        val dialog = InputDialogHomeFragment()
        val args = Bundle()
        args.putString("savedText", savedText)
        dialog.arguments = args
        dialog.show(parentFragmentManager, "InputDialogFragment")
    }

    private fun showDownloadDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Do you want to save the image?")
            .setPositiveButton("Yes") { _, _ ->
                saveImageViewContent()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun saveImageViewContent() {
        val imageView = binding.imageView
        val drawable = imageView.drawable
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap

            val filename = "saved_image_${System.currentTimeMillis()}.png"
            val file = File(requireContext().getExternalFilesDir(null), filename)
            try {
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                // 如果是 Android 10 或以上，可以使用 MediaStore 保存到公共目录
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    saveImageToMediaStore(bitmap, filename)
                } else {
                    Toast.makeText(requireContext(), "Image saved: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Drawable is not a BitmapDrawable", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageToMediaStore(bitmap: Bitmap, filename: String) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/WiseBrush")
        }

        val uri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            try {
                val outputStream = requireContext().contentResolver.openOutputStream(it)
                outputStream?.let { stream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    stream.flush()
                    stream.close()
                }
                Toast.makeText(requireContext(), "Image saved to gallery", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to save to MediaStore", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}