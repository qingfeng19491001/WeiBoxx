package com.example.weiboxx.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey val id: String,
    val username: String,
    val avatar: String,
    val timestamp: String,
    val source: String,
    val content: String,
    val likes: Int,
    val comments: Int,
    val shares: Int,
    val isLiked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()// 添加此字段用于排序

)