package com.example.weiboxx.live

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weiboxx.data.model.Comment
import com.example.weiboxx.databinding.ActivityLiveStreamBinding
import com.example.weiboxx.live.adapter.CommentsAdapter

class LiveStreamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLiveStreamBinding
    private val viewModel: LiveStreamViewModel by viewModels()
    private lateinit var commentsAdapter: CommentsAdapter
    private var commentList = mutableListOf<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveStreamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        commentsAdapter = CommentsAdapter(commentList)
        binding.rvComments.apply {
            layoutManager = LinearLayoutManager(this@LiveStreamActivity)
            adapter = commentsAdapter
        }
    }

    private fun setupObservers() {
        // 1. 观察直播信息的变化
        viewModel.liveStreamInfo.observe(this, Observer { info ->
            binding.tvHostName.text = info.hostName
            binding.tvViewerCount.text = info.viewerCount
            // 使用 Glide 加载头像
            Glide.with(this).load(info.hostAvatarUrl).circleCrop().into(binding.ivHostAvatar)
        })

        // 2. 观察评论列表的变化
        viewModel.comments.observe(this, Observer { newComments ->
            val oldSize = commentList.size
            commentList.clear()
            commentList.addAll(newComments)
            // 通知适配器数据已更改
            commentsAdapter.notifyDataSetChanged()
            // 自动滚动到最新评论
            binding.rvComments.scrollToPosition(commentList.size - 1)
        })
    }

    private fun setupClickListeners() {
        // 点击关注按钮
        binding.btnFollow.setOnClickListener {
            viewModel.onFollowClicked()
            Toast.makeText(this, "已关注", Toast.LENGTH_SHORT).show()
        }

        // 点击发送按钮
        binding.btnSend.setOnClickListener {
            val commentText = binding.etCommentInput.text.toString()
            if (commentText.isNotBlank()) {
                viewModel.sendComment(commentText)
                binding.etCommentInput.text.clear() // 清空输入框
            }
        }
    }
}