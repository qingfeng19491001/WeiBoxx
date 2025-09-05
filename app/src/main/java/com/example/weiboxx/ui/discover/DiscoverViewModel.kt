package com.example.weiboxx.ui.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weiboxx.data.model.HotTopic
import com.example.weiboxx.data.repository.HotTopicRepository
import kotlinx.coroutines.launch

class DiscoverViewModel : ViewModel() {

    private val repository = HotTopicRepository()

    // 热搜数据
    private val _hotTopics = MutableLiveData<List<HotTopic>>()
    val hotTopics: LiveData<List<HotTopic>> = _hotTopics

    // 加载状态
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // 错误信息
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // 当前选中的标签
    private val _selectedTab = MutableLiveData<TabType>()
    val selectedTab: LiveData<TabType> = _selectedTab

    init {
        _selectedTab.value = TabType.HOT
    }

    fun loadHotTopics() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val topics = when (_selectedTab.value) {
                    TabType.HOT -> repository.getHotTopics()
                    TabType.HOT_QUESTION -> repository.getHotQuestions()
                    TabType.HOT_FORWARD -> repository.getHotForwards()
                    TabType.PUBLISH -> repository.getPublishTopics()
                    TabType.INDEX -> repository.getIndexTopics()
                    else -> repository.getHotTopics()
                }
                _hotTopics.value = topics
            } catch (e: Exception) {
                _error.value = "加载失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onTabSelected(tabType: TabType) {
        if (_selectedTab.value != tabType) {
            _selectedTab.value = tabType
            loadHotTopics()
        }
    }

    fun onSearchClicked(query: String) {
        viewModelScope.launch {
            // 处理搜索逻辑
            try {
                val searchResults = repository.searchTopics(query)
                _hotTopics.value = searchResults
            } catch (e: Exception) {
                _error.value = "搜索失败: ${e.message}"
            }
        }
    }

    fun onHotTopicClicked(hotTopic: HotTopic) {
        // 处理热搜项点击事件
        // 可以导航到详情页面或执行其他操作
    }
}