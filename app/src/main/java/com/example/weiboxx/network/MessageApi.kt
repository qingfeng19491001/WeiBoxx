package com.example.weiboxx.network

import com.example.weiboxx.data.model.Message
import retrofit2.Response
import retrofit2.http.*

/**
 * 消息相关的API接口
 * 当您需要真实的网络请求时可以使用
 */
interface MessageApi {

    /**
     * 获取消息列表
     */
    @GET("messages")
    suspend fun getMessages(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): Response<MessageResponse>

    /**
     * 搜索消息
     */
    @GET("messages/search")
    suspend fun searchMessages(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): Response<MessageResponse>

    /**
     * 标记消息为已读
     */
    @POST("messages/{messageId}/read")
    suspend fun markMessageAsRead(
        @Path("messageId") messageId: String
    ): Response<BaseResponse>

    /**
     * 删除消息
     */
    @DELETE("messages/{messageId}")
    suspend fun deleteMessage(
        @Path("messageId") messageId: String
    ): Response<BaseResponse>
}

/**
 * 消息响应数据类
 */
data class MessageResponse(
    val code: Int,
    val message: String,
    val data: MessageData
)

data class MessageData(
    val messages: List<Message>,
    val total: Int,
    val hasMore: Boolean
)

/**
 * 基础响应数据类
 */
data class BaseResponse(
    val code: Int,
    val message: String,
    val data: Any? = null
)

/**
 * 网络服务配置（示例）
 */
/*
object NetworkConfig {

    private const val BASE_URL = "https://your-api-domain.com/api/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val messageApi: MessageApi = retrofit.create(MessageApi::class.java)
}
*/