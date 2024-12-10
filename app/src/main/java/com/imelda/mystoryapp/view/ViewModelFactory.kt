package com.imelda.mystoryapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.imelda.mystoryapp.data.StoryRepository
import com.imelda.mystoryapp.data.UserRepository
import com.imelda.mystoryapp.di.Injection
import com.imelda.mystoryapp.view.home.HomeViewModel
import com.imelda.mystoryapp.view.home.TambahStoryViewModel
import com.imelda.mystoryapp.view.login.LoginViewModel
import com.imelda.mystoryapp.view.main.MainViewModel
import com.imelda.mystoryapp.view.signup.SignupViewModel
import kotlinx.coroutines.runBlocking

class ViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(TambahStoryViewModel::class.java) -> {
                TambahStoryViewModel(storyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val userRepository = Injection.provideUserRepository(context)
                val storyRepository = runBlocking { Injection.provideStoryRepository(context) }

                ViewModelFactory(userRepository, storyRepository).also {
                    INSTANCE = it
                }
            }
        }
    }
}