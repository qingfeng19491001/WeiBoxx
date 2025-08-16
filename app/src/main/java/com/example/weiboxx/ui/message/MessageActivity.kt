package com.example.weiboxx.ui.message

import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weiboxx.R
import com.example.weiboxx.data.model.Message

/**
 * 消息页面Activity
 */
class MessageActivity : AppCompatActivity(), MessageAdapter.OnMessageClickListener {

    private val viewModel: MessageViewModel by viewModels()
    private lateinit var adapter: MessageAdapter
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        initViews()
        initAdapter()
        observeViewModel()
        setupSearchView()
    }

    private fun initViews() {
        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerViewMessages)

        // 设置RecyclerView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MessageActivity)
            setHasFixedSize(true)
        }
    }

    private fun initAdapter() {
        adapter = MessageAdapter(this)
        adapter.setOnMessageClickListener(this)
        recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        // 观察消息列表变化
        viewModel.filteredMessagesLiveData.observe(this) { messages ->
            messages?.let {
                adapter.updateMessages(it)
            }
        }

        // 观察加载状态
        viewModel.isLoadingLiveData.observe(this) { isLoading ->
            isLoading?.let {
                // TODO: 显示/隐藏加载指示器
                // progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        // 观察错误信息
        viewModel.errorLiveData.observe(this) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchMessages(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchMessages(newText ?: "")
                return false
            }
        })

        // 清除搜索时的处理
        searchView.setOnCloseListener {
            viewModel.searchMessages("")
            false
        }
    }

    override fun onMessageClick(message: Message, position: Int) {
        // 处理消息点击事件
        Toast.makeText(this, "点击了: ${message.title}", Toast.LENGTH_SHORT).show()

        // TODO: 根据消息类型跳转到相应页面
        when (message.type) {
            Message.MessageType.PERSONAL -> {
                // 跳转到@我的页面
            }
            Message.MessageType.COMMENT -> {
                // 跳转到评论页面
            }
            Message.MessageType.LIKE -> {
                // 跳转到点赞页面
            }
            Message.MessageType.NEWS -> {
                // 跳转到新闻详情页面
            }
            Message.MessageType.NOTIFICATION -> {
                // 跳转到通知详情页面
            }
            Message.MessageType.GROUP -> {
                // 跳转到群推荐页面
            }
            Message.MessageType.LIVE -> {
                // 跳转到直播页面
            }
            Message.MessageType.STORY -> {
                // 跳转到小秘书页面
            }
            Message.MessageType.SCHOOL -> {
                // 跳转到学校页面
            }
        }

        // 标记消息为已读
        viewModel.markMessageAsRead(message.id)
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
            return
        }
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        // 刷新消息
        viewModel.refreshMessages()
    }
}