package com.example.weiboxx.data.model

data class HotTopic(
    val id: Long,
    val leftTitle: String,
    val rightTitle: String,
    val rank: Int = 0,
    val isHot: Boolean = false,
    val isNew: Boolean = false,
    val searchCount: Long = 0,
    val category: String = ""
)