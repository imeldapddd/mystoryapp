package com.imelda.mystoryapp.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.imelda.mystoryapp.databinding.ActivityLoginBinding
import com.imelda.mystoryapp.view.ViewModelFactory
import com.imelda.mystoryapp.view.home.HomeActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        observeLoginResult()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.login(email, password)
        }
    }

    private fun observeLoginResult() {
        viewModel.loginResult.observe(this) { result ->
            if (result.isSuccessful) {
                val token = result.token
                Log.d("LoginActivity", "Token yang diterima: $token")
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("TOKEN", token) // Pastikan token dikirim ke activity berikutnya
                startActivity(intent)
                finish()
            } else {
                showErrorDialog(result.message)
            }
        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Login Gagal")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}