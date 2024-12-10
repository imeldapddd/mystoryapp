package com.imelda.mystoryapp.data.retrofit

import com.imelda.mystoryapp.data.response.FileUploadResponse
import com.imelda.mystoryapp.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface StoryApiService {
    @GET("stories")
    suspend fun getAllStories(): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): FileUploadResponse
}