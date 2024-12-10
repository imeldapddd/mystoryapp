package com.imelda.mystoryapp.view.home

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.imelda.mystoryapp.R
import com.imelda.mystoryapp.data.StoryRepository
import com.imelda.mystoryapp.data.pref.UserPreference
import com.imelda.mystoryapp.data.pref.dataStore
import com.imelda.mystoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class TambahStoryActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var descriptionEditText: EditText
    private lateinit var buttonGallery: Button
    private lateinit var buttonCamera: Button
    private lateinit var buttonSave: Button

    private var imageUri: Uri? = null
    private lateinit var storyRepository: StoryRepository
    private lateinit var userPreference: UserPreference
    private val IMAGE_PICK_CODE = 1000
    private val CAMERA_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_story)

        imageView = findViewById(R.id.imageView)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        buttonGallery = findViewById(R.id.buttonGallery)
        buttonCamera = findViewById(R.id.buttonCamera)
        buttonSave = findViewById(R.id.buttonSave)

        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        buttonGallery.setOnClickListener { openGallery() }
        buttonCamera.setOnClickListener { openCamera() }
        buttonSave.setOnClickListener { saveStory() }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun openCamera() {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "New Photo")
            put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
        }
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        }
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun saveStory() {
        val description = descriptionEditText.text.toString()
        if (imageUri == null || description.isEmpty()) {
            showToast("Please provide both an image and a description")
            return
        }

        lifecycleScope.launch {
            try {
                val token = userPreference.getSession().first().token

                if (token.isEmpty()) {
                    showToast("Token is missing!")
                    return@launch
                }

                val apiService = ApiConfig.getStoryApiService(token)
                storyRepository = StoryRepository.getInstance(apiService, userPreference)

                val file = uriToFile(imageUri!!, this@TambahStoryActivity)
                val lat: Float? = null
                val lon: Float? = null

                val response = storyRepository.uploadStory(file, description, lat, lon)
                if (!response.error) {
                    showToast("Story uploaded successfully!")
                    finish()
                } else {
                    showToast("Failed to upload story: ${response.message}")
                }
            } catch (e: Exception) {
                showToast("An error occurred: ${e.message}")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_PICK_CODE) {
                imageUri = data?.data
                imageView.setImageURI(imageUri)
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                imageView.setImageURI(imageUri)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val myFile = File.createTempFile("temp_", ".jpg", context.cacheDir)
        val inputStream = contentResolver.openInputStream(selectedImg) ?: return myFile
        val outputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()
        return myFile
    }
}