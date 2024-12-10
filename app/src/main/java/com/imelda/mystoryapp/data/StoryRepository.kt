package com.imelda.mystoryapp.data

import com.imelda.mystoryapp.data.pref.UserPreference
import com.imelda.mystoryapp.data.response.FileUploadResponse
import com.imelda.mystoryapp.data.response.StoryResponse
import com.imelda.mystoryapp.data.retrofit.StoryApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class StoryRepository private constructor(
    private val storyapiService: StoryApiService,
    private val userPreference: UserPreference
) {

    suspend fun getAllStories(): StoryResponse {
        return storyapiService.getAllStories()
    }

    suspend fun uploadStory(
        photoFile: File,
        description: String,
        lat: Float? = null,
        lon: Float? = null
    ): FileUploadResponse {

        val descriptionRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), description)
        val photoRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), photoFile)
        val photoPart = MultipartBody.Part.createFormData("photo", photoFile.name, photoRequestBody)
        val latRequestBody = lat?.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it.toString()) }
        val lonRequestBody = lon?.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it.toString()) }

        return storyapiService.uploadStory(
            description = descriptionRequestBody,
            photo = photoPart,
            lat = latRequestBody,
            lon = lonRequestBody
        )
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            storyapiService: StoryApiService,
            userPreference: UserPreference
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(storyapiService, userPreference)
            }.also { instance = it }
    }
}
