package com.example.weiboxx.ui

import com.example.weiboxx.data.model.Post

/**
 * 主界面的UI状态
 */
data class MainUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMoreData: Boolean = true,
    val error: String? = null
)