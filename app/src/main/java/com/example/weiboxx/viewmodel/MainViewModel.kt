package com.example.weiboxx.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 主页面ViewModel，负责管理UI状态
 */
class MainViewModel(private val repository: com.example.weiboxx.data.repository.PostRepositoryImpl) : ViewModel() {
    
    // 当前选中的顶部标签（推荐/关注）
    private val _currentTab = MutableLiveData<Int>(0)
    val currentTab: LiveData<Int> = _currentTab
    
    // 当前选中的底部导航项
    private val _currentBottomNav = MutableLiveData<Int>(0)
    val currentBottomNav: LiveData<Int> = _currentBottomNav
    
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
}