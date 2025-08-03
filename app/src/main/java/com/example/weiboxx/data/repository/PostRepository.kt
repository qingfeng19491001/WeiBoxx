package com.example.weiboxx.data.repository

import com.example.weiboxx.data.model.Post
import kotlinx.coroutines.CoroutineDispatcher

interface PostRepository {
    suspend fun getPosts(): List<Post>
    suspend fun likePost(postId: String): Boolean
    suspend fun sharePost(postId: String): Boolean
    fun launch(dispatcher: kotlinx.coroutines.CoroutineDispatcher, function: () -> kotlin.Any)
}