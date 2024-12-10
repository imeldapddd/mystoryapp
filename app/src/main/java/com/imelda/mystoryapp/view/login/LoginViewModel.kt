package com.imelda.mystoryapp.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imelda.mystoryapp.data.UserRepository
import com.imelda.mystoryapp.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> get() = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.loginUser(email, password)

                if (response.error) {
                    _loginResult.value = LoginResult(false, response.message)
                } else {
                    val user = UserModel(
                        email = email,
                        token = response.loginResult.token,
                        userId = response.loginResult.userId,
                        name = response.loginResult.name,
                        isLogin = true
                    )
                    repository.saveSession(user)
                    _loginResult.value = LoginResult(true, "Login berhasil", response.loginResult.token)
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login gagal: ${e.localizedMessage}", e)
                _loginResult.value = LoginResult(false, "Terjadi kesalahan, coba lagi nanti.")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

data class LoginResult(val isSuccessful: Boolean, val message: String, val token: String? = null)