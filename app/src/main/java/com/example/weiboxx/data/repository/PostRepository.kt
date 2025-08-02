package com.example.weiboxx.data.repository

import com.example.weiboxx.data.model.Post
import kotlinx.coroutines.CoroutineDispatcher

interface PostRepository {
    suspend fun getPosts(): List<Post>
    suspend fun likePost(postId: String): Boolean
    suspend fun sharePost(postId: String): Boolean
    suspend fun addPost(content: String): Boolean // 添加这个方法
    // 删除不需要的 launch 方法

}