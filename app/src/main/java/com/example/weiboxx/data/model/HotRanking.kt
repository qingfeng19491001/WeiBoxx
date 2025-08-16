package com.example.weiboxx.data.model

data class HotRanking(
    val rank: Int,
    val title: String,
    val subtitle: String? = null,
    val category: String = "TOP26"
)
