package com.imelda.mystoryapp.view.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.imelda.mystoryapp.R
import com.imelda.mystoryapp.data.pref.UserPreference
import com.imelda.mystoryapp.data.pref.dataStore
import com.imelda.mystoryapp.databinding.HomeActivityBinding
import com.imelda.mystoryapp.view.ViewModelFactory
import com.imelda.mystoryapp.view.login.LoginActivity
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: HomeActivityBinding
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext)
    }
    private val adapter: HomeAdapter by lazy { HomeAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupRecyclerView()
        observeData()
        fetchData()

        binding.fab.setOnClickListener {
            val intent = Intent(this, TambahStoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_keluar -> {
                logout()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeData() {
        homeViewModel.stories.observe(this) { stories ->
            adapter.submitList(stories)
        }

        homeViewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchData() {
        homeViewModel.getAllStories()
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun logout() {
        val userPreference = UserPreference.getInstance(applicationContext.dataStore)
        lifecycleScope.launch {
            userPreference.logout()
        }

        Toast.makeText(this, "Keluar berhasil!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}