package com.example.weiboxx.data.model

data class Message(
    val id: String,
    val title: String,
    val content: String,
    val time: String,
    val avatarUrl: String = "",
    val avatarResId: Int = 0,
    val isVerified: Boolean = false,
    val type: MessageType
) {
    enum class MessageType {
        PERSONAL,    // @我的
        COMMENT,     // 评论
        LIKE,        // 赞
        NEWS,        // 新闻
        NOTIFICATION, // 通知
        GROUP,       // 群推荐
        LIVE,        // 直播
        STORY,       // 小秘书
        SCHOOL       // 学校
    }

    /**
     * 获取头像背景颜色
     */
    fun getAvatarBackgroundColor(): Int {
        return when (type) {
            MessageType.PERSONAL -> 0xFF4FC3F7.toInt() // 蓝色
            MessageType.COMMENT -> 0xFF66BB6A.toInt()  // 绿色
            MessageType.LIKE -> 0xFFFF9800.toInt()     // 橙色
            MessageType.NEWS -> 0xFFE53935.toInt()     // 红色
            MessageType.NOTIFICATION -> 0xFF42A5F5.toInt() // 浅蓝色
            MessageType.GROUP -> 0xFFFF9800.toInt()    // 橙色
            MessageType.LIVE -> 0xFFE53935.toInt()     // 红色
            MessageType.STORY -> 0xFFFF5722.toInt()    // 深橙色
            MessageType.SCHOOL -> 0xFF1976D2.toInt()   // 深蓝色
        }
    }
}
