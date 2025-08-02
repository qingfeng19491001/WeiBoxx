package com.example.weiboxx.data.model

data class Post(
    val id: String,
    val username: String,
    val content: String,
    val timestamp: String,
    val source: String,
    val likes: Int = 0,
    val comments: Int = 0,
    val shares: Int = 0,
    val avatar: String? = null
)
