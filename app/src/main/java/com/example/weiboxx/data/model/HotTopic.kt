package com.example.weiboxx.data.model

data class HotTopic(
    val id: Int,
    val title: String,
    val rank: Int,
    val isHot: Boolean = false,
    val isNew: Boolean = false,
    val category: String? = null,
    val hasVideo: Boolean = false
)
