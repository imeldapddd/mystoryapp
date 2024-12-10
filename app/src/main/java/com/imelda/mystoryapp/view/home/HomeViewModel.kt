package com.imelda.mystoryapp.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imelda.mystoryapp.data.StoryRepository
import com.imelda.mystoryapp.data.response.ListStoryItem
import kotlinx.coroutines.launch

class HomeViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getAllStories() {
        viewModelScope.launch {
            try {
                val response = storyRepository.getAllStories()
                if (!response.error) {
                    _stories.value = response.listStory
                } else {
                    _errorMessage.value = response.message
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Terjadi Sebuah Kesalahan"
            }
        }
    }
}