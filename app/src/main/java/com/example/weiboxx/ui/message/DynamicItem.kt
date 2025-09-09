package com.example.weiboxx.ui.message

data class DynamicItem(
    val id: Long,
    val username: String,
    val avatar: String,
    val content: String,
    val time: String,
    val hasImage: Boolean = false,
    val hasVideo: Boolean = false,
    val videoDuration: String = "",
    val commentCount: Int = 0,
    val repostCount: Int = 0,
    val likeCount: Int = 0
)