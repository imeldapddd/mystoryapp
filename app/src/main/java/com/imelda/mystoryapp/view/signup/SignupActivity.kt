package com.imelda.mystoryapp.view.signup

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.imelda.mystoryapp.databinding.ActivitySignupBinding
import com.imelda.mystoryapp.view.ViewModelFactory

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
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

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)  // Use getInstance() here
        signupViewModel = ViewModelProvider(this, factory)[SignupViewModel::class.java]
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signupViewModel.register(name, email, password)

            signupViewModel.registerResponse.observe(this) { response ->
                if (!response.error) {
                    AlertDialog.Builder(this).apply {
                        setTitle("Yeah!")
                        setMessage("Akun berhasil dibuat. Yuk, login dan belajar coding!")
                        setPositiveButton("Lanjut") { _, _ -> finish() }
                        create()
                        show()
                    }
                } else {
                    Toast.makeText(this, "Pendaftaran gagal: ${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}