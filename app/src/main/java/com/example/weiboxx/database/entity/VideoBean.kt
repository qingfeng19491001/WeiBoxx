package com.example.weiboxx.database.entity

data class VideoBean(
    val videoUrl: String = "",
    val coverUrl: String = "",
    val username: String = "",
    val description: String = "",
    val musicName: String = "",
    val avatarUrl: String = "",
    var likeCount: Int = 0,
    var commentCount: Int = 0,
    var shareCount: Int = 0,
    var isLiked: Boolean = false,
    var isFollowed: Boolean = false
)
