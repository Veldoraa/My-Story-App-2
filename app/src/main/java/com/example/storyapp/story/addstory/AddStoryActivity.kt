package com.example.storyapp.story.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.storyapp.story.camera.CameraActivity
import com.example.storyapp.data.local.SharedPreferences
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.data.Repo.Result
import com.example.storyapp.ui.mainstory.MainActivity
import com.example.storyapp.utils.*
import com.example.storyapp.utils.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var file: File? = null
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Don't have  a permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                CODE_PERMISSIONS
            )
        }
        binding.btnAddCamera.setOnClickListener { cameraX() }
        binding.btnAddGallery.setOnClickListener { imageFromGaleri() }
        binding.btnUpload.setOnClickListener { uploadImage() }
    }

    private val imageByGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            file = myFile
            val resultImage = rotateBitmap(
                BitmapFactory.decodeFile(file?.path),
            )
            binding.ivPreview.setImageBitmap(resultImage)
        }
    }

    private fun imageFromGaleri() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        imageByGallery.launch(chooser)
    }

    private fun cameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFiles = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            file = myFiles
            val result = rotateBitmap(
                BitmapFactory.decodeFile(file?.path),
                isBackCamera
            )

            binding.ivPreview.setImageBitmap(result)
        }
    }

    private fun uploadImage() {
        val factory = ViewModelFactory.getInstance(this)
        val viewModel: AddStoryViewModels by viewModels { factory }
        sharedPreferences = SharedPreferences(this)
        if (file != null && !binding.etAdd.text.isNullOrEmpty()) {
            val files = reduceFileImage(file as File)
            val description = binding.etAdd.text.toString()
            val requestImageFile = files.asRequestBody("image/jpeg".toMediaTypeOrNull())
            viewModel.uploadStory(sharedPreferences.getToken(), description, files)
                .observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding.progressAdd.visible()
                            }
                            is Result.Success -> {
                                binding.progressAdd.gone()
                                Toast.makeText(this, "Success Upload story", Toast.LENGTH_SHORT)
                                    .show()
                                val i = Intent(this, MainActivity::class.java)
                                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(i)
                            }
                            is Result.Error -> {
                                binding.progressAdd.gone()
                                Toast.makeText(this, "upload failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val CODE_PERMISSIONS = 10
    }
}