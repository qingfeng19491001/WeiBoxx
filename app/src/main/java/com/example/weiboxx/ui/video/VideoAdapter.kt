package com.example.weiboxx.ui.video

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.jzvd.JzvdStd
import com.example.weiboxx.R
import com.example.weiboxx.database.entity.VideoBean

class VideoAdapter(
    private val context: Context,
    private val videoList: List<VideoBean>
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private var onVideoItemClickListener: OnVideoItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]

        with(holder) {
            //暂时不设置真实视频，只显示占位符
            videoPlayer.setUp("", "", JzvdStd.SCREEN_FULLSCREEN)

            // 设置文本内容
            tvUsername.text = "@${video.username}"
            tvDescription.text = video.description

            // 设置统计数据
            tvLikeCount.text = formatCount(video.likeCount)
            tvCommentCount.text = formatCount(video.commentCount)
            tvShareCount.text = formatCount(video.shareCount)

            // 设置点击事件
            ivAvatar.setOnClickListener {
                onVideoItemClickListener?.onAvatarClick(position, video)
            }

            tvLikeCount.setOnClickListener {
                onVideoItemClickListener?.onLikeClick(position, video)
            }

            tvCommentCount.setOnClickListener {
                onVideoItemClickListener?.onCommentClick(position, video)
            }

            tvShareCount.setOnClickListener {
                onVideoItemClickListener?.onShareClick(position, video)
            }
        }
    }

    override fun getItemCount(): Int = videoList.size

    private fun formatCount(count: Int): String {
        return when {
            count < 1000 -> count.toString()
            count < 10000 -> String.format("%.1fk", count / 1000.0f)
            else -> String.format("%.1fw", count / 10000.0f)
        }
    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoPlayer: JzvdStd = itemView.findViewById(R.id.videoplayer)
        val tvUsername: TextView = itemView.findViewById(R.id.tv_username)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)

        val tvLikeCount: TextView = itemView.findViewById(R.id.tv_like_count)
        val tvCommentCount: TextView = itemView.findViewById(R.id.tv_comment)
        val tvShareCount: TextView = itemView.findViewById(R.id.tv_share_count)
        val ivAvatar: ImageView = itemView.findViewById(R.id.iv_avatar)
    }

    fun setOnVideoItemClickListener(listener: OnVideoItemClickListener) {
        this.onVideoItemClickListener = listener
    }
}