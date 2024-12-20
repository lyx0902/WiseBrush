package com.example.bottomnavigation1.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.bottomnavigation1.R
import com.example.bottomnavigation1.databinding.FragmentHomeBinding
import com.example.bottomnavigation1.model.GenerateRequest
import com.example.bottomnavigation1.repository.ImageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.toggleButton.setOnClickListener {
            showInputDialog()
        }

        binding.button.setOnClickListener {
            val imageRepository = ImageRepository
            var sharedPreferences = requireContext().getSharedPreferences("InputDialogPrefs", Context.MODE_PRIVATE)
            //1
            var generateRequest = GenerateRequest(
                requireView().findViewById<EditText>(R.id.editText1).text.toString(),
                requireView().findViewById<EditText>(R.id.editText2).text.toString(),
                sharedPreferences.getString("savedText1", "7.5").toString().toDouble(),
                sharedPreferences.getString("savedText2", "50").toString().toInt(),
                sharedPreferences.getString("savedText3", "512").toString().toInt(),
            )

            imageRepository.textToImg(requireContext(), generateRequest ){ result ->
                    result.onSuccess { file ->
                        // 文件保存成功，显示图像文件
                        var imageFilePath = file
                        var imageName = imageFilePath + ".png"
                        val imageView: ImageView = requireView().findViewById(R.id.imageView1)
                        val imagePath = getWiseBrushImagePath(requireContext(), imageName)
                        if (imagePath != null) {
                            loadImageFromPath(imageView, imagePath)
                        } else {
                            Toast.makeText(requireContext(), "Image Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                    result.onFailure { exception ->
                        Log.e("Error", "API request failed", exception)
                    }

                }
            }

        return root
    }

    private fun getWiseBrushImagePath(context: Context, fileName: String): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val selection = "${MediaStore.Images.Media.RELATIVE_PATH} LIKE ? AND ${MediaStore.Images.Media.DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf("%Pictures%", fileName)
        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                return it.getString(columnIndex)
            }
        }
        return null
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

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImage.launch(intent)
    }

    fun loadImageFromPath(imageView: ImageView, imagePath: String) {
        val bitmap = BitmapFactory.decodeFile(imagePath)
        imageView.setImageBitmap(bitmap)
    }

    private fun loadImageFromUri(imageUri: Uri) {
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
                    binding.imageView1.setImageBitmap(scaledBitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}