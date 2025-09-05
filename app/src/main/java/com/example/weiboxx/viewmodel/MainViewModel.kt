package com.example.weiboxx.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import com.example.weiboxx.data.model.Post
import com.example.weiboxx.data.repository.PostRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(private val repository: PostRepositoryImpl) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    private val _selectedBottomNavIndex = MutableStateFlow(0)
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
                Log.d("MainViewModel", "Loading posts...")
                val posts = repository.getPosts()
                Log.d("MainViewModel", "Loaded ${posts.size} posts")
                _uiState.value = _uiState.value.copy(
                    posts = posts,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                Log.e("MainViewModel", "Failed to load posts: ${e.message}", e)
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
                Log.e("MainViewModel", "Failed to refresh posts: ${e.message}", e)
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
                Log.e("MainViewModel", "Failed to load more posts: ${e.message}", e)
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
                Log.e("MainViewModel", "Failed to like post: ${e.message}", e)
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
                Log.e("MainViewModel", "Failed to share post: ${e.message}", e)
                _toastMessage.value = "分享失败: ${e.message}"
            }
        }
    }

    fun addPost(content: String) {
        viewModelScope.launch {
            try {
                // 生成唯一ID（包含时间戳）
                val postId = "local_${System.currentTimeMillis()}"

                // 直接更新UI状态（不等待网络）
                val newPost = createLocalPost(content, postId)
                val updatedPosts = listOf(newPost) + _uiState.value.posts
                _uiState.value = _uiState.value.copy(posts = updatedPosts)

                // 异步保存到数据库
                val success = repository.addPost(content, postId)
                if (!success) {
                    _toastMessage.value = "发布保存失败"
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Failed to add post: ${e.message}", e)
                _toastMessage.value = "发布失败: ${e.message}"
            }
        }
    }

    // 创建本地帖子对象
    private fun createLocalPost(content: String, id: String): Post {
        val currentTime = System.currentTimeMillis()
        val timeFormat = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())

        return Post(
            id = id,
            username = "当前用户",  // 替换为实际用户名
            avatar = "",
            timestamp = timeFormat.format(Date(currentTime)),
            source = "手机客户端",
            content = content,
            likes = 0,
            comments = 0,
            shares = 0,
            isLiked = false,
            createdAt = currentTime
        )
    }

    fun switchTab(index: Int) {
        _currentTab.value = index
    }

    fun switchBottomNav(index: Int) {
        _selectedBottomNavIndex.value = index
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearToastMessage() {
        _toastMessage.value = ""
    }
}