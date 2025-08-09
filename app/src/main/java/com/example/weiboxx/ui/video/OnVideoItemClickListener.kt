package com.example.weiboxx.ui.video

import com.example.weiboxx.database.entity.VideoBean

interface OnVideoItemClickListener {
    fun onAvatarClick(position: Int, video: VideoBean)
    fun onLikeClick(position: Int, video: VideoBean)
    fun onCommentClick(position: Int, video: VideoBean)
    fun onShareClick(position: Int, video: VideoBean)
}