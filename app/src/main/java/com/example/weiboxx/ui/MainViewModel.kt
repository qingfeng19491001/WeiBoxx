package com.example.weiboxx.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weiboxx.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weiboxx.data.repository.PostRepositoryImpl
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: PostRepositoryImpl) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    private val _selectedBottomNavIndex = MutableStateFlow(0) // 底部导航选中状态
    val selectedBottomNavIndex: StateFlow<Int> = _selectedBottomNavIndex.asStateFlow()

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val posts = repository.getPosts()
                _uiState.value = _uiState.value.copy(
                    posts = posts,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _toastMessage.value = "加载失败: ${e.message}"
            }
        }
    }

    fun refreshPosts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            try {
                val posts = repository.refreshPosts()
                _uiState.value = _uiState.value.copy(
                    posts = posts,
                    isRefreshing = false,
                    error = null
                )
                _toastMessage.value = "刷新成功"
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    error = e.message
                )
                _toastMessage.value = "刷新失败"
            }
        }
    }

    fun loadMorePosts() {
        if (_uiState.value.isLoadingMore) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingMore = true)
            try {
                val morePosts = repository.loadMorePosts()
                val currentPosts = _uiState.value.posts.toMutableList()
                currentPosts.addAll(morePosts)

                _uiState.value = _uiState.value.copy(
                    posts = currentPosts,
                    isLoadingMore = false,
                    hasMoreData = morePosts.isNotEmpty()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingMore = false
                )
                _toastMessage.value = "加载更多失败"
            }
        }
    }

    fun likePost(postId: String) {
        viewModelScope.launch {
            try {
                val success = repository.likePost(postId)
                if (success) {
                    // 更新UI中的点赞状态
                    val updatedPosts = _uiState.value.posts.map { post ->
                        if (post.id == postId) {
                            post.copy(likes = post.likes + 1)
                        } else {
                            post
                        }
                    }
                    _uiState.value = _uiState.value.copy(posts = updatedPosts)
                    _toastMessage.value = "点赞成功"
                } else {
                    _toastMessage.value = "点赞失败"
                }
            } catch (e: Exception) {
                _toastMessage.value = "点赞失败: ${e.message}"
            }
        }
    }

    fun sharePost(postId: String) {
        viewModelScope.launch {
            try {
                val success = repository.sharePost(postId)
                if (success) {
                    _toastMessage.value = "分享成功"
                } else {
                    _toastMessage.value = "分享失败"
                }
            } catch (e: Exception) {
                _toastMessage.value = "分享失败: ${e.message}"
            }
        }
    }

    fun switchTab(index: Int) {
        _currentTab.value = index
    }

    // 切换底部导航
    fun switchBottomNav(index: Int) {
        _selectedBottomNavIndex.value = index
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}