package com.example.weiboxx.ui.home

import android.animation.ObjectAnimator
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.weiboxx.R
import com.example.weiboxx.data.model.Post

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val ivAvatar: ImageView = itemView.findViewById(R.id.iv_avatar)
    private val tvUsername: TextView = itemView.findViewById(R.id.tv_username)
    private val tvTime: TextView = itemView.findViewById(R.id.tv_time)
    private val tvSource: TextView = itemView.findViewById(R.id.tv_source)
    private val tvContent: TextView = itemView.findViewById(R.id.tv_content)
    private val viewImagePlaceholder: View = itemView.findViewById(R.id.view_image_placeholder)
    private val tvLikeCount: TextView = itemView.findViewById(R.id.tv_like_count)
    private val tvCommentCount: TextView = itemView.findViewById(R.id.tv_comment_count)
    private val tvShareCount: TextView = itemView.findViewById(R.id.tv_share_count)
    private val llLike: View = itemView.findViewById(R.id.ll_like)
    private val llComment: View = itemView.findViewById(R.id.ll_comment)
    private val llShare: View = itemView.findViewById(R.id.ll_share)
    private val ivLike: ImageView = llLike.findViewById(R.id.iv_like)

    private var isLiked = false
    private var currentPost: Post? = null

    fun bind(post: Post, onItemAction: (String, String) -> Unit) {
        currentPost = post

        tvUsername.text = post.username
        tvTime.text = post.timestamp   // 如需要格式化，自行实现
        tvSource.text = post.source

//        // 头像
//        Glide.with(itemView.context)
//            .load(post.avatar)
//            .circleCrop()
//            .placeholder(R.drawable.default_avatar)
//            .into(ivAvatar)

        tvContent.text = post.content
        tvLikeCount.text = formatCount(post.likes)
        tvCommentCount.text = formatCount(post.comments)
        tvShareCount.text = formatCount(post.shares)

        llLike.setOnClickListener {
            animateLike()
            onItemAction(post.id, "like")
        }
        llComment.setOnClickListener { onItemAction(post.id, "comment") }
        llShare.setOnClickListener { animateShare(); onItemAction(post.id, "share") }

        tvContent.setOnLongClickListener {
            copyTextToClipboard(post.content)
            true
        }
    }

    private fun animateLike() {
        isLiked = !isLiked
        val colorRes = if (isLiked) R.color.like_red else R.color.gray
        tvLikeCount.setTextColor(ContextCompat.getColor(itemView.context, colorRes))
        ivLike.setColorFilter(ContextCompat.getColor(itemView.context, colorRes))
        
        // 点赞动画
        val scaleX = ObjectAnimator.ofFloat(ivLike, "scaleX", 1f, 1.2f, 1f)
        val scaleY = ObjectAnimator.ofFloat(ivLike, "scaleY", 1f, 1.2f, 1f)
        scaleX.duration = 300
        scaleY.duration = 300
        scaleX.start()
        scaleY.start()
        
        // 更新点赞数显示
        val currentLikes = currentPost?.likes ?: 0
        val newLikes = if (isLiked) currentLikes + 1 else currentLikes - 1
        tvLikeCount.text = newLikes.toString()
        // 注意：实际的Post对象更新应该在ViewModel中处理
    }
    
    private fun animateShare() {
        // 分享按钮动画
        val rotation = ObjectAnimator.ofFloat(llShare, "rotation", 0f, 360f)
        rotation.duration = 500
        rotation.start()
    }
    
    private fun copyTextToClipboard(text: String) {
        val clipboardManager = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("微博内容", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(itemView.context, "内容已复制到剪贴板", Toast.LENGTH_SHORT).show()
    }
    
    private fun formatCount(count: Int): String {
        return if (count >= 10000) {
            String.format("%.1f万", count / 10000.0)
        } else {
            count.toString()
        }
    }
}