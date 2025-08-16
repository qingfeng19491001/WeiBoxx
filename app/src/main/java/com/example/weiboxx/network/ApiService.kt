package com.example.weiboxx.network

import com.example.weiboxx.data.model.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("posts")
    suspend fun getPosts(@Query("page") page: Int): Response<List<Post>>

    @POST("posts/{id}/like")
    suspend fun likePost(@Path("id") id: String): Response<ApiResponse>

    @POST("posts/{id}/share")
    suspend fun sharePost(@Path("id") postId: String): Response<ApiResponse>


}