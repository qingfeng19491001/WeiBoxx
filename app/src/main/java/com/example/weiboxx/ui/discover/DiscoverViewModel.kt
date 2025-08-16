package com.example.weiboxx.ui.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weiboxx.data.model.HotRanking
import com.example.weiboxx.data.model.HotTopic
import com.example.weiboxx.data.model.Post
import com.example.weiboxx.data.model.User

class DiscoverViewModel : ViewModel() {

    private val _hotTopics = MutableLiveData<List<HotTopic>>()
    val hotTopics: LiveData<List<HotTopic>> = _hotTopics

    private val _hotRankings = MutableLiveData<List<HotRanking>>()
    val hotRankings: LiveData<List<HotRanking>> = _hotRankings

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _selectedTab = MutableLiveData<Int>()
    val selectedTab: LiveData<Int> = _selectedTab

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        _isLoading.value = true

        // Mock data - in real app, this would come from repository
        val mockHotTopics = listOf(
            HotTopic(1, "韩立结婴", 1),
            HotTopic(2, "B站崩了", 2),
            HotTopic(3, "珂尼娜与吴宣仪的...", 3),
            HotTopic(4, "广西人该了解的...", 4, hasVideo = true),
            HotTopic(5, "iPhone充电环习惯", 5),
            HotTopic(6, "杨幂说孩子就这...", 6, isHot = true),
            HotTopic(7, "广东100%的蚊子出...", 7),
            HotTopic(8, "四六级查分", 8),
            HotTopic(9, "吴谨言被生日彩带...", 9),
            HotTopic(10, "沈月拍了满满一墙...", 10),
            HotTopic(11, "贺峻霖四级成绩", 11, category = "娱")
        )

        val mockRankings = listOf(
            HotRanking(1, "中餐厅全员轮岗", "后厨救场，全力保障，新任大厨们，放心出餐吧～")
        )




        _hotTopics.value = mockHotTopics
        _hotRankings.value = mockRankings
        _selectedTab.value = 0 // Default to "热点" tab
        _isLoading.value = false
    }

    fun onTabSelected(tabIndex: Int) {
        _selectedTab.value = tabIndex
        // In real app, load different data based on selected tab
        when (tabIndex) {
            0 -> loadHotTopics()
            1 -> loadHotQuestions()
            2 -> loadHotForwards()
            3 -> loadPublishContent()
            4 -> loadIndexData()
        }
    }

    private fun loadHotTopics() {
        // Load hot topics data
    }

    private fun loadHotQuestions() {
        // Load hot questions data
    }

    private fun loadHotForwards() {
        // Load hot forwards data
    }

    private fun loadPublishContent() {
        // Load publish content data
    }

    private fun loadIndexData() {
        // Load index data
    }

    fun onSearchClicked() {
        // Handle search action
    }

    fun onHotTopicClicked(hotTopic: HotTopic) {
        // Handle hot topic click
    }

    fun onPostClicked(post: Post) {
        // Handle post click
    }

    fun onUserClicked(user: User) {
        // Handle user click
    }

    fun refreshData() {
        loadInitialData()
    }
}