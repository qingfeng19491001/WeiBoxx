package com.example.weiboxx.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weiboxx.data.repository.PostRepository
import com.example.weiboxx.data.repository.PostRepositoryImpl

class MainViewModelFactory(private val repository: PostRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository as PostRepositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}