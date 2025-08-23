package com.example.weiboxx.live

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weiboxx.data.model.Comment
import com.example.weiboxx.data.model.LiveStreamInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LiveStreamViewModel : ViewModel() {

    // 直播间信息 - 使用 private MutableLiveData + public LiveData 封装
    private val _liveStreamInfo = MutableLiveData<LiveStreamInfo>()
    val liveStreamInfo: LiveData<LiveStreamInfo> = _liveStreamInfo

    // 评论列表
    private val _comments = MutableLiveData<MutableList<Comment>>(mutableListOf())
    val comments: LiveData<MutableList<Comment>> = _comments

    // 模拟的评论ID
    private var commentIdCounter = 0

    init {
        // ViewModel 创建时，开始加载初始数据并模拟接收新评论
        loadInitialData()
        startReceivingComments()
    }

    private fun loadInitialData() {
        // 在真实应用中，这里会进行网络请求
        val info = LiveStreamInfo(
            hostName = "主持人高杰",
            hostAvatarUrl = "https://example.com/avatar.png",
            viewerCount = "85.1万观看",
            heatScore = "热度榜·1.3万热度"
        )
        _liveStreamInfo.value = info
    }

    private fun startReceivingComments() {
        // 使用协程模拟不断有新评论进来
        viewModelScope.launch {
            val sampleComments = listOf(
                Comment(++commentIdCounter, "布要熬夜啦", "周深好棒!", 7),
                Comment(++commentIdCounter, "潘美祖", "不好听！戴个佛珠吧", 7),
                Comment(++commentIdCounter, "潘美祖", "..................", 7),
                Comment(++commentIdCounter, "潘美祖", "周深改个名字叫周末吧", 7)
            )
            for (comment in sampleComments) {
                delay(2000) // 每2秒来一条新评论
                addComment(comment)
            }
        }
    }

    // 添加一条新评论到列表中
    private fun addComment(comment: Comment) {
        val currentList = _comments.value ?: mutableListOf()
        currentList.add(comment)
        // 限制列表最大数量，防止内存溢出
        if (currentList.size > 100) {
            currentList.removeAt(0)
        }
        _comments.value = currentList
    }

    // 供View调用，模拟发送评论
    fun sendComment(content: String) {
        val newComment = Comment(
            id = ++commentIdCounter,
            userName = "我",
            content = content,
            userLevel = 10
        )
        addComment(newComment)
    }

    // 模拟点击关注按钮的逻辑
    fun onFollowClicked() {
        // 在这里处理关注/取消关注的逻辑
        // 例如：调用Repository更新服务器状态
        println("关注按钮被点击了!")
    }
}