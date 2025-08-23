package com.example.weiboxx.data.model

data class Comment(
    val id: Int,
    val userName: String,
    val content: String,
    val userLevel: Int // 模拟用户等级/徽章
)

data class LiveStreamInfo(
    val hostName: String,
    val hostAvatarUrl: String,
    val viewerCount: String,
    val heatScore: String
)
