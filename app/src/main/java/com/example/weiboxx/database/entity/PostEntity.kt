package com.example.weiboxx.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.System

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val username: String,
    val content: String,
    val timestamp: String,
    val source: String,
    val likes: Int,
    val comments: Int,
    val shares: Int,
    val avatar: String?,
    val isLiked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
