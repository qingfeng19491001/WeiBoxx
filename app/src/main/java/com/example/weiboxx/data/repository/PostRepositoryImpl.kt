package com.example.weiboxx.data.repository

import android.content.Context
import com.example.weiboxx.data.model.Post
import com.example.weiboxx.database.dao.PostDao
import com.example.weiboxx.database.entity.PostEntity
import com.example.weiboxx.network.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class PostRepositoryImpl(
    private val apiService: ApiService,
    private val postDao: PostDao,
    private val context: Context
) : PostRepository {

    private var currentPage = 1
    private val postsCache = mutableListOf<Post>()

    override suspend fun getPosts(): List<Post> = try {
        val localPosts = postDao.getAllPosts().first()
        if (localPosts.isEmpty()) {
            loadFromNetwork()
        } else {
            withContext(Dispatchers.IO) {
                loadFromNetwork()
            }
            // 如果本地有数据，直接返回本地
            localPosts
        }
    } catch (e: Exception) {
        getMockPosts()
    } as List<Post>

    private suspend fun loadFromNetwork(): List<Post> {
        val response = apiService.getPosts(currentPage)
        if (response.isSuccessful) {
            val posts = response.body() ?: emptyList()
            // 保存到本地数据库
            postDao.insertPosts(posts.map { it.toEntity() })
            return posts
        } else {
            throw Exception("网络请求失败")
        }
    }

    private fun getMockPosts(): List<Post> {
        return listOf(
            Post(
                id = "1",
                username = "访客357",
                content = "想和我道歉嘛过去儿；骨传导狗把二个女四千猎野分析vueglhx体验q 我先去购物不想去我的女儿不去次不次都不武器过度过渡期我跟不可去我对积对公嘴气引我去打个的嘴案做的花都我去过隐忽过得去嘿嘿气我老爷不愿兴趣；新的感情我GV的感情味道回了个高度辣度合敏地球老千个不亲局低了理财辞藻去送佛音像不诱钢素材必须功不此次不起来了\n\n#群我推##背了背#",
                timestamp = "7-11",
                source = "微博视频号",
                likes = 11700,
                comments = 11700,
                shares = 11700
            ),
            Post(
                id = "2",
                username = "用户123",
                content = "今天天气真不错，出去走走心情都变好了 #好心情 #阳光明媚",
                timestamp = "7-12",
                source = "手机客户端",
                likes = 5600,
                comments = 890,
                shares = 234
            )
        )
    }

    override suspend fun likePost(postId: String): Boolean {
        return try {
            val response = apiService.likePost(postId)
            if (response.isSuccessful) {
                // 更新本地数据库
                postDao.updateLikeStatus(postId, true, 1)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            // 网络失败，只更新本地状态
            postDao.updateLikeStatus(postId, true, 1)
            false
        }
    }

    override suspend fun sharePost(postId: String): Boolean {
        return try {
            val response = apiService.sharePost(postId)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    override fun launch(
        dispatcher: CoroutineDispatcher,
        function: () -> Any
    ) {
        TODO("Not yet implemented")
    }

    suspend fun loadMorePosts(): List<Post> {
        currentPage++
        return try {
            loadFromNetwork()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun refreshPosts(): List<Post> {
        currentPage = 1
        //postDao.clearAll()
        return loadFromNetwork()
    }
}

// 扩展函数
fun PostEntity.toPost(): Post {
    return Post(
        id = id,
        username = username,
        content = content,
        timestamp = timestamp,
        source = source,
        likes = likes,
        comments = comments,
        shares = shares,
        avatar = avatar
    )
}

fun Post.toEntity(): PostEntity {
    return PostEntity(
        id = id,
        username = username,
        content = content,
        timestamp = timestamp,
        source = source,
        likes = likes,
        comments = comments,
        shares = shares,
        avatar = avatar
    )
}