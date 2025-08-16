package com.example.weiboxx.data.model

data class VideoResponse(
    val id: Int,
    val width: Int,
    val height: Int,
    val url: String,
    val image: String,
    val duration: Int,
    val user: User,
    val video_files: List<VideoFile>,
    val video_pictures: List<VideoPicture>
)

data class VideoFile(
    val id: Int,
    val quality: String,
    val file_type: String,
    val width: Int?,
    val height: Int?,
    val link: String
)

data class User(
    val id: Int,
    val name: String,
    val url: String
)

data class VideoPicture(
    val id: Int,
    val picture: String,
    val nr: Int
)