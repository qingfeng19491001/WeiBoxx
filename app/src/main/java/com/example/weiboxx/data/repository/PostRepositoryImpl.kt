package com.example.weiboxx.data.repository

import android.content.Context
import android.util.Log
import com.example.weiboxx.data.model.Post
import com.example.weiboxx.database.dao.PostDao
import com.example.weiboxx.network.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class PostRepositoryImpl(
    private val apiService: ApiService,
    private val postDao: PostDao,
    private val context: Context
) : PostRepository {

    private var currentPage = 1
    private val postsCache = mutableListOf<Post>()


    // 修改 getPosts() 方法
    override suspend fun getPosts(): List<Post> = withContext(Dispatchers.IO) {
        try {
            // 优先返回本地数据
            val localPosts = postDao.getAllPosts().first()

            // 异步加载网络数据但不覆盖本地发布
            try {
                val networkPosts = loadFromNetwork()
                // 只插入ID不在本地数据库中的新帖子
                val newPosts = networkPosts.filter { newPost ->
                    localPosts.none { it.id == newPost.id }
                }
                if (newPosts.isNotEmpty()) {
                    postDao.insertPosts(newPosts)
                }
            } catch (e: Exception) {
                Log.w("PostRepository", "网络更新失败: ${e.message}")
            }

            // 返回合并后的最新数据
            postDao.getAllPosts().first()
        } catch (e: Exception) {
            Log.e("PostRepository", "Failed to get posts: ${e.message}", e)
            getMockPosts()
        }
    }

    private suspend fun loadFromNetwork(): List<Post> {
        try {
            val response = apiService.getPosts(currentPage)
            if (response.isSuccessful) {
                val posts = response.body() ?: emptyList()
                postDao.insertPosts(posts)
                return posts
            } else {
                throw Exception("网络请求失败: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "Network load failed: ${e.message}", e)
            throw e
        }
    }

    private fun getMockPosts(): List<Post> {
        return listOf(
            Post(
                id = "mock_1",
                username = "访客357",
                avatar = "",
                content = "想和我道歉嘛过去儿；骨传导狗把二个女四千猎野分析vueglhx体验q 我先去购物不想去我的女儿不去次不次都不武器过度过渡期我跟不可去我对积对公嘴气引我去打个的嘴案做的花都我去过隐忽过得去嘿嘿气我老爷不愿兴趣；新的感情我GV的感情味道回了个高度辣度合敏地球老千个不亲局低了理财辞藻去送佛音像不诱钢素材必须功不此次不起来了\n\n#群我推##背了背#",
                timestamp = "7-11",
                source = "微博视频号",
                likes = 11700,
                comments = 11700,
                shares = 11700
            ),
            Post(
                id = "mock_2",
                username = "用户123",
                avatar = "",
                content = "今天天气真不错，出去走走心情都变好了 #好心情 #阳光明媚",
                timestamp = "7-12",
                source = "手机客户端",
                likes = 5600,
                comments = 890,
                shares = 234
            )
        )
    }

    override suspend fun likePost(postId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            // 先尝试网络请求
            val response = apiService.likePost(postId)
            if (response.isSuccessful) {
                postDao.updateLikeStatus(postId, true, 1)
                true
            } else {
                // 网络失败也更新本地状态
                postDao.updateLikeStatus(postId, true, 1)
                true // 返回true让用户看到即时反馈
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "Like post failed: ${e.message}", e)
            // 网络失败，只更新本地状态
            try {
                postDao.updateLikeStatus(postId, true, 1)
                true
            } catch (dbE: Exception) {
                Log.e("PostRepository", "Database update failed: ${dbE.message}", dbE)
                false
            }
        }
    }

    override suspend fun sharePost(postId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = apiService.sharePost(postId)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("PostRepository", "Share post failed: ${e.message}", e)
            false
        }
    }

    override suspend fun addPost(content: String): Boolean {
        TODO("Not yet implemented")
    }

    suspend fun addPost(content: String, postId: String): Boolean {
        try {
            Log.d("PostRepository", "Starting to add post with content: $content")

            // 创建新的Post对象
            val currentTime = System.currentTimeMillis()
            val timeFormat = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())

            val newPost = Post(
                id = "local_$currentTime", // 使用时间戳作为ID
                username = "我", // 或者从用户配置中获取
                avatar = "",
                timestamp = timeFormat.format(Date(currentTime)),
                source = "手机客户端",
                content = content,
                likes = 0,
                comments = 0,
                shares = 0,
                isLiked = false,
                createdAt = currentTime
            )

            Log.d("PostRepository", "Created new post: $newPost")

            // 插入到数据库
            postDao.insertPost(newPost)
            Log.d("PostRepository", "Successfully inserted post to database")

            // 可选：尝试同步到服务器（失败也不影响本地显示）
            try {
                // 这里可以添加网络同步逻辑
                // apiService.createPost(newPost)
            } catch (e: Exception) {
                Log.w("PostRepository", "Failed to sync to server: ${e.message}")
                // 不影响本地成功状态
            }

            return true

        } catch (e: Exception) {
            Log.e("PostRepository", "Add post failed: ${e.message}", e)
            return false
        }
    }

    suspend fun loadMorePosts(): List<Post> = withContext(Dispatchers.IO) {
        currentPage++
        try {
            loadFromNetwork()
        } catch (e: Exception) {
            Log.e("PostRepository", "Load more posts failed: ${e.message}", e)
            emptyList()
        }
    }

    // 修改 refreshPosts() 方法
    suspend fun refreshPosts(): List<Post> = withContext(Dispatchers.IO) {
        currentPage = 1
        try {
            val networkPosts = loadFromNetwork()

            // 获取当前本地帖子（特别是本地发布的）
            val localPosts = postDao.getAllPosts().first()
            val localPostIds = localPosts.map { it.id }.toSet()

            // 筛选出需要保留的本地帖子（ID不在网络数据中）
            val postsToKeep = localPosts.filter { it.id.startsWith("local_") }

            // 合并数据：保留的本地帖子 + 网络数据
            val mergedPosts = postsToKeep + networkPosts

            // 更新数据库
            postDao.deleteAll()
            postDao.insertPosts(mergedPosts)

            mergedPosts
        } catch (e: Exception) {
            Log.e("PostRepository", "刷新失败: ${e.message}", e)
            // 失败时返回当前数据库数据（而不是空列表）
            postDao.getAllPosts().first()
        }
    }

    // 移除不需要的launch方法
}