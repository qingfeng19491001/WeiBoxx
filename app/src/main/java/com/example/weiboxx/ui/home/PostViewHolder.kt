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
//    private val ivLike: ImageView = llLike.findViewById(R.id.iv_like) ?: ImageView(itemView.context)

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
        tvLikeCount.text = post.likes.toString()
        tvCommentCount.text = post.comments.toString()
        tvShareCount.text = post.shares.toString()

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

    private fun animateLike() { /* 同上 */ }
    private fun animateShare() { /* 同上 */ }
    private fun copyTextToClipboard(text: String) { /* 同上 */ }
}