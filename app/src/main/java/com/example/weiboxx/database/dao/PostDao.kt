package com.example.weiboxx.database.dao

import androidx.room.*
import com.example.weiboxx.database.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("UPDATE posts SET isLiked = :isLiked, likes = likes + :increment WHERE id = :postId")
    suspend fun updateLikeStatus(postId: String, isLiked: Boolean, increment: Int)

    @Query("DELETE FROM posts")
    suspend fun deleteAll()
}