package com.example.weiboxx.ui.home

import androidx.recyclerview.widget.DiffUtil
import com.example.weiboxx.data.model.Post

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Post, newItem: Post): Any? {
        val changes = mutableListOf<String>()

        if (oldItem.likes != newItem.likes) {
            changes.add("likes")
        }
        if (oldItem.comments != newItem.comments) {
            changes.add("comments")
        }
        if (oldItem.shares != newItem.shares) {
            changes.add("shares")
        }

        return if (changes.isNotEmpty()) changes else null
    }


}
