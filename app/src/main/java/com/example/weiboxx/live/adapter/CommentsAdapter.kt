package com.example.weiboxx.live.adapter
import com.example.weiboxx.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weiboxx.data.model.Comment

class CommentsAdapter(private val comments: List<Comment>) :
    RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userLevel: TextView = view.findViewById(R.id.tvUserLevel)
        val userName: TextView = view.findViewById(R.id.tvUserName)
        val commentContent: TextView = view.findViewById(R.id.tvCommentContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.userLevel.text = "榜${comment.userLevel}"
        holder.userName.text = "${comment.userName}："
        holder.commentContent.text = comment.content
    }

    override fun getItemCount() = comments.size
}


