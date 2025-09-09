package com.example.weiboxx.ui.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.weiboxx.R

class DynamicsAdapter : RecyclerView.Adapter<DynamicsAdapter.DynamicViewHolder>() {

    private val dynamics = mutableListOf<DynamicItem>()

    fun updateData(newDynamics: List<DynamicItem>) {
        dynamics.clear()
        dynamics.addAll(newDynamics)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dynamic, parent, false)
        return DynamicViewHolder(view)
    }

    override fun onBindViewHolder(holder: DynamicViewHolder, position: Int) {
        holder.bind(dynamics[position])
    }

    override fun getItemCount(): Int = dynamics.size

    inner class DynamicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivAvatar: ImageView = itemView.findViewById(R.id.iv_avatar)
        private val tvUsername: TextView = itemView.findViewById(R.id.tv_username)
        private val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        private val tvContent: TextView = itemView.findViewById(R.id.tv_content)
        private val ivImage: ImageView = itemView.findViewById(R.id.iv_image)
        private val llVideo: LinearLayout = itemView.findViewById(R.id.ll_video)
        private val tvVideoDuration: TextView = itemView.findViewById(R.id.tv_video_duration)
        private val tvComment: TextView = itemView.findViewById(R.id.tv_comment)
        private val tvRepost: TextView = itemView.findViewById(R.id.tv_repost)
        private val tvLike: TextView = itemView.findViewById(R.id.tv_like)
        private val ivMore: ImageView = itemView.findViewById(R.id.iv_more)

        fun bind(dynamic: DynamicItem) {
            tvUsername.text = dynamic.username
            tvTime.text = dynamic.time
            tvContent.text = dynamic.content

            // 设置头像背景色（模拟不同用户）
            val colors = listOf(
                "#FF6B6B", "#4ECDC4", "#45B7D1", "#96CEB4", "#FFEAA7", "#DDA0DD"
            )
            val colorIndex = (dynamic.id % colors.size).toInt()
            ivAvatar.setBackgroundColor(android.graphics.Color.parseColor(colors[colorIndex]))

            // 显示/隐藏图片
            if (dynamic.hasImage) {
                ivImage.visibility = View.VISIBLE
                ivImage.setBackgroundColor(android.graphics.Color.parseColor("#F0F0F0"))
            } else {
                ivImage.visibility = View.GONE
            }

            // 显示/隐藏视频
            if (dynamic.hasVideo) {
                llVideo.visibility = View.VISIBLE
                tvVideoDuration.text = dynamic.videoDuration
            } else {
                llVideo.visibility = View.GONE
            }

            // 设置点击事件
            tvComment.setOnClickListener {
                Toast.makeText(itemView.context, "评论 ${dynamic.username} 的微博", Toast.LENGTH_SHORT).show()
            }

            tvRepost.setOnClickListener {
                Toast.makeText(itemView.context, "转发 ${dynamic.username} 的微博", Toast.LENGTH_SHORT).show()
            }

            tvLike.setOnClickListener {
                Toast.makeText(itemView.context, "点赞 ${dynamic.username} 的微博", Toast.LENGTH_SHORT).show()
            }

            ivMore.setOnClickListener {
                Toast.makeText(itemView.context, "更多操作", Toast.LENGTH_SHORT).show()
            }

            llVideo.setOnClickListener {
                if (dynamic.hasVideo) {
                    Toast.makeText(itemView.context, "播放视频", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}