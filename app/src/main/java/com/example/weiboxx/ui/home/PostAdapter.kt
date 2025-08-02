package com.example.weiboxx.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weiboxx.R
import com.example.weiboxx.data.model.Post

class PostAdapter(
    private val onItemAction: (String, String) -> Unit
) : ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position), onItemAction)
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUsername: TextView = itemView.findViewById(R.id.tv_username)
        private val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        private val tvSource: TextView = itemView.findViewById(R.id.tv_source)
        private val tvContent: TextView = itemView.findViewById(R.id.tv_content)
        private val tvLikeCount: TextView = itemView.findViewById(R.id.tv_like_count)
        private val tvCommentCount: TextView = itemView.findViewById(R.id.tv_comment_count)
        private val tvShareCount: TextView = itemView.findViewById(R.id.tv_share_count)
        private val llLike: View = itemView.findViewById(R.id.ll_like)
        private val llShare: View = itemView.findViewById(R.id.ll_share)

        fun bind(post: Post, onItemAction: (String, String) -> Unit) {
            tvUsername.text = post.username
            tvTime.text = post.timestamp
            tvSource.text = post.source
            tvContent.text = post.content
            tvLikeCount.text = "${post.likes / 1000.0}万"
            tvCommentCount.text = "${post.comments / 1000.0}万"
            tvShareCount.text = "${post.shares / 1000.0}万"

            llLike.setOnClickListener { onItemAction(post.id, "like") }
            llShare.setOnClickListener { onItemAction(post.id, "share") }
        }
    }
}

