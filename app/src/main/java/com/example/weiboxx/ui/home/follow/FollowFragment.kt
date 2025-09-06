package com.example.weiboxx.ui.home.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weiboxx.R
import com.example.weiboxx.data.model.Post

class FollowFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyPostAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        
        // 创建适配器并设置数据
        adapter = MyPostAdapter(createMyPosts())
        recyclerView.adapter = adapter
        
        // 设置点击事件
        adapter.setOnItemClickListener(object : MyPostAdapter.OnItemClickListener {
            override fun onLikeClick(position: Int) {
                Toast.makeText(context, "点赞成功", Toast.LENGTH_SHORT).show()
            }
            
            override fun onCommentClick(position: Int) {
                Toast.makeText(context, "评论功能开发中", Toast.LENGTH_SHORT).show()
            }
            
            override fun onForwardClick(position: Int) {
                Toast.makeText(context, "转发功能开发中", Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    // 创建用户自己发的微博数据
    private fun createMyPosts(): List<MyPost> {
        return listOf(
            MyPost(
                username = "用户7765265592",
                content = "今天天气真好，出去走走放松一下心情！#生活记录# #心情分享#",
                timestamp = "刚刚",
                source = "来自微博",
                imageUrl = null,
                likeCount = 0,
                commentCount = 0,
                forwardCount = 0
            ),
            MyPost(
                username = "用户7765265592",
                content = "分享一张今天拍的照片，希望大家喜欢！",
                timestamp = "10分钟前",
                source = "来自微博",
                imageUrl = null,
                likeCount = 10,
                commentCount = 5,
                forwardCount = 2
            ),
            MyPost(
                username = "用户7765265592",
                content = "学习了一天的编程，感觉收获很多，继续加油！#学习# #编程# #Android开发#",
                timestamp = "1小时前",
                source = "来自微博",
                imageUrl = null,
                likeCount = 20,
                commentCount = 8,
                forwardCount = 5
            )
        )
    }
    
    // 用户自己发的微博数据类
    data class MyPost(
        val username: String,
        val content: String,
        val timestamp: String,
        val source: String,
        val imageUrl: String?,
        val likeCount: Int = 0,
        val commentCount: Int = 0,
        val forwardCount: Int = 0
    )
    
    // 用户自己发的微博适配器
    inner class MyPostAdapter(private val posts: List<MyPost>) : 
        RecyclerView.Adapter<MyPostAdapter.MyPostViewHolder>() {
        
        // 定义点击事件接口
        interface OnItemClickListener {
            fun onLikeClick(position: Int)
            fun onCommentClick(position: Int)
            fun onForwardClick(position: Int)
        }
        
        private var listener: OnItemClickListener? = null
        
        fun setOnItemClickListener(listener: OnItemClickListener) {
            this.listener = listener
        }
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_post, parent, false)
            return MyPostViewHolder(view)
        }
        
        override fun onBindViewHolder(holder: MyPostViewHolder, position: Int) {
            holder.bind(posts[position])
        }
        
        override fun getItemCount(): Int = posts.size
        
        inner class MyPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val tvUsername: TextView = itemView.findViewById(R.id.tv_username)
            private val tvContent: TextView = itemView.findViewById(R.id.tv_content)
            private val tvTime: TextView = itemView.findViewById(R.id.tv_time)
            private val tvSource: TextView = itemView.findViewById(R.id.tv_source)
            private val tvLikeCount: TextView = itemView.findViewById(R.id.tv_like_count)
            private val tvCommentCount: TextView = itemView.findViewById(R.id.tv_comment_count)
            private val tvShareCount: TextView = itemView.findViewById(R.id.tv_share_count)
            private val llLike = itemView.findViewById<View>(R.id.ll_like)
            private val llComment = itemView.findViewById<View>(R.id.ll_comment)
            private val llShare = itemView.findViewById<View>(R.id.ll_share)
            
            init {
                // 设置点击事件
                llLike.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener?.onLikeClick(position)
                    }
                }
                
                llComment.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener?.onCommentClick(position)
                    }
                }
                
                llShare.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener?.onForwardClick(position)
                    }
                }
            }
            
            fun bind(post: MyPost) {
                tvUsername.text = post.username
                tvContent.text = post.content
                tvTime.text = post.timestamp
                tvSource.text = post.source
                
                // 设置点赞、评论、转发数量
                tvLikeCount.text = formatCount(post.likeCount)
                tvCommentCount.text = formatCount(post.commentCount)
                tvShareCount.text = formatCount(post.forwardCount)
            }
            
            private fun formatCount(count: Int): String {
                return if (count >= 10000) {
                    String.format("%.1f万", count / 10000.0)
                } else if (count > 0) {
                    count.toString()
                } else {
                    "0"
                }
            }
        }
    }
}