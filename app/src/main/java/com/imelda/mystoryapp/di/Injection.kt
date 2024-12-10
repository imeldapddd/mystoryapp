package com.imelda.mystoryapp.di

import android.content.Context
import com.imelda.mystoryapp.data.StoryRepository
import com.imelda.mystoryapp.data.UserRepository
import com.imelda.mystoryapp.data.pref.UserPreference
import com.imelda.mystoryapp.data.pref.dataStore
import com.imelda.mystoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first

object Injection{
    fun provideUserRepository(context: Context): UserRepository{
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getAuthApiService()
        return UserRepository.getInstance(pref, apiService)
    }

    suspend fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = pref.getSession().first()
        val apiService = ApiConfig.getStoryApiService(user.token)
        return StoryRepository.getInstance(apiService, pref)
}}