package com.example.weiboxx.ui.message

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.weiboxx.data.model.Message

/**
 * 消息页面 ViewModel
 */
class MessageViewModel : ViewModel() {

    private val _messagesLiveData = MutableLiveData<List<Message>>()
    val messagesLiveData: MutableLiveData<List<Message>> = _messagesLiveData

    private val _filteredMessagesLiveData = MutableLiveData<List<Message>>()
    val filteredMessagesLiveData: MutableLiveData<List<Message>> = _filteredMessagesLiveData

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: MutableLiveData<Boolean> = _isLoadingLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: MutableLiveData<String> = _errorLiveData

    private var allMessages: List<Message> = emptyList()
    private var currentSearchQuery: String = ""

    init {
        loadMessages()
    }

    /**
     * 加载消息数据
     */
    fun loadMessages() {
        viewModelScope.launch {
            _isLoadingLiveData.value = true

            try {
                // 模拟网络请求延迟
                delay(1000)

                val messages = withContext(Dispatchers.IO) {
                    createMockMessages()
                }

                allMessages = messages
                _messagesLiveData.value = messages
                _filteredMessagesLiveData.value = messages
                _isLoadingLiveData.value = false

            } catch (e: Exception) {
                _errorLiveData.value = "加载失败: ${e.message}"
                _isLoadingLiveData.value = false
            }
        }
    }

    /**
     * 搜索消息
     */
    fun searchMessages(query: String) {
        currentSearchQuery = query

        if (allMessages.isEmpty()) return

        val filteredMessages = if (query.isBlank()) {
            allMessages
        } else {
            val lowerQuery = query.lowercase().trim()
            allMessages.filter { message ->
                message.title.lowercase().contains(lowerQuery) ||
                        message.content.lowercase().contains(lowerQuery)
            }
        }

        _filteredMessagesLiveData.value = filteredMessages
    }

    /**
     * 刷新消息
     */
    fun refreshMessages() {
        loadMessages()
    }

    /**
     * 创建模拟数据
     */
    private fun createMockMessages(): List<Message> {
        return listOf(
            Message(
                id = "1",
                title = "@我的",
                content = "",
                time = "",
                type = Message.MessageType.PERSONAL
            ),
            Message(
                id = "2",
                title = "评论",
                content = "",
                time = "",
                type = Message.MessageType.COMMENT
            ),
            Message(
                id = "3",
                title = "赞",
                content = "",
                time = "",
                type = Message.MessageType.LIKE
            ),
            Message(
                id = "4",
                title = "新浪新闻",
                content = "普京特朗普会晤未达成协议！",
                time = "09:08",
                isVerified = true,
                type = Message.MessageType.NEWS
            ),
            Message(
                id = "5",
                title = "服务通知",
                content = "超话社区：校友发帖啦！",
                time = "8-13",
                type = Message.MessageType.NOTIFICATION
            ),
            Message(
                id = "6",
                title = "群推荐",
                content = "加入感兴趣的粉丝群，开启热聊模式",
                time = "7-26",
                type = Message.MessageType.GROUP
            ),
            Message(
                id = "7",
                title = "微博直播",
                content = "感谢关注微博直播！",
                time = "7-26",
                isVerified = true,
                type = Message.MessageType.LIVE
            ),
            Message(
                id = "8",
                title = "微博小秘书",
                content = "参加高考作文大赛，赢现金红包",
                time = "6-9",
                isVerified = true,
                type = Message.MessageType.STORY
            ),
            Message(
                id = "9",
                title = "桂林电子科技大学",
                content = "欢迎关注桂林电子科技大学，需要咨询...",
                time = "4-24",
                isVerified = true,
                type = Message.MessageType.SCHOOL
            )
        )
    }

    /**
     * 标记消息为已读
     */
    fun markMessageAsRead(messageId: String) {
        // TODO: 实现标记消息已读的逻辑
    }

    /**
     * 删除消息
     */
    fun deleteMessage(messageId: String) {
        allMessages = allMessages.filter { it.id != messageId }
        searchMessages(currentSearchQuery) // 重新过滤显示
    }
}