package com.example.weiboxx.database.dao

import androidx.room.*
import com.example.weiboxx.data.model.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY createdAt DESC") // 使用 createdAt 排序更合理
    fun getAllPosts(): Flow<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<Post>)  // 添加批量插入方法

    @Query("UPDATE posts SET isLiked = :isLiked, likes = likes + :increment WHERE id = :postId")
    suspend fun updateLikeStatus(postId: String, isLiked: Boolean, increment: Int)

    @Query("DELETE FROM posts")
    suspend fun deleteAll()
}