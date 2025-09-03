package com.example.weiboxx.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.weiboxx.data.model.Post

/**
 * 表示主界面UI状态的数据类
 */
data class MainUiState(
    val posts: List<Post> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null
)

/**
 * 主页面ViewModel，负责管理UI状态
 */
class MainViewModel(private val repository: com.example.weiboxx.data.repository.PostRepositoryImpl) : ViewModel() {
    
    // UI状态
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    
    // 当前选中的顶部标签（推荐/关注）
    private val _currentTab = MutableLiveData<Int>(0)
    val currentTab: LiveData<Int> = _currentTab
    
    // 当前选中的底部导航项
    private val _currentBottomNav = MutableLiveData<Int>(0)
    val currentBottomNav: LiveData<Int> = _currentBottomNav
    
    // Toast消息
    private val _toastMessage = MutableLiveData<String>("")
    val toastMessage: LiveData<String> = _toastMessage
    
    init {
        // 初始化时加载数据
        loadPosts()
    }
    
    /**
     * 设置当前选中的顶部标签
     * @param position 0:推荐, 1:关注
     */
    fun setCurrentTab(position: Int) {
        _currentTab.value = position
    }
    
    /**
     * 设置当前选中的底部导航项
     * @param position 0:首页, 1:视频, 2:发现, 3:消息, 4:我的
     */
    fun setCurrentBottomNav(position: Int) {
        _currentBottomNav.value = position
    }
    
    /**
     * 加载帖子数据
     */
    fun loadPosts() {
        // 设置加载状态
        _uiState.value = _uiState.value.copy(isRefreshing = true)
        
        // 这里应该是从repository加载数据的逻辑
        // 暂时使用模拟数据
        val mockPosts = repository.getMockPosts()
        
        // 更新UI状态
        _uiState.value = _uiState.value.copy(
            posts = mockPosts,
            isRefreshing = false
        )
    }
    
    /**
     * 清除错误信息
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}