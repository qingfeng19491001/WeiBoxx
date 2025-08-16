package com.example.weiboxx.data.repository

import com.example.weiboxx.data.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * 消息数据仓库
 */
class MessageRepository {

    /**
     * 获取消息列表
     */
    suspend fun getMessages(): List<Message> = withContext(Dispatchers.IO) {
        // 模拟网络请求延迟
        delay(1000)

        // 模拟数据，实际项目中这里应该是网络请求
        createMockMessages()
    }

    /**
     * 搜索消息
     */
    suspend fun searchMessages(query: String, messages: List<Message>): List<Message> = withContext(Dispatchers.IO) {
        if (query.isBlank()) {
            messages
        } else {
            val lowerQuery = query.lowercase().trim()
            messages.filter { message ->
                message.title.lowercase().contains(lowerQuery) ||
                        message.content.lowercase().contains(lowerQuery)
            }
        }
    }

    /**
     * 标记消息为已读
     */
    suspend fun markMessageAsRead(messageId: String): Boolean = withContext(Dispatchers.IO) {
        // 模拟网络请求
        delay(500)
        // TODO: 实际实现标记已读的网络请求
        true
    }

    /**
     * 删除消息
     */
    suspend fun deleteMessage(messageId: String): Boolean = withContext(Dispatchers.IO) {
        // 模拟网络请求
        delay(500)
        // TODO: 实际实现删除消息的网络请求
        true
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
}