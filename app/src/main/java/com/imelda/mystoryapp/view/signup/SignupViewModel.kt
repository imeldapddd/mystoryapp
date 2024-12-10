package com.imelda.mystoryapp.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imelda.mystoryapp.data.UserRepository
import com.imelda.mystoryapp.data.response.RegisterResponse
import kotlinx.coroutines.launch

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> get() = _registerResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = userRepository.registerUser(name, email, password)
                _registerResponse.value = response
            } catch (e: Exception) {
                Log.e("SignupViewModel", "Registration failed", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}