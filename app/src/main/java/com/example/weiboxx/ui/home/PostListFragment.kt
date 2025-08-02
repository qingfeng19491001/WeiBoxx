package com.example.weiboxx.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weiboxx.R
import com.example.weiboxx.ui.MainViewModel
import kotlinx.coroutines.launch
class PostListFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var adapter: PostAdapter
    private var isLoadingMore = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initViewModel()
        observeViewModel()
        setupScrollListener()
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)

        adapter = PostAdapter { postId, action ->
            when (action) {
                "like" -> viewModel.likePost(postId)
                "share" -> viewModel.sharePost(postId)
                "comment" -> {
                    // 处理评论点击
                    Toast.makeText(requireContext(), "评论功能开发中", Toast.LENGTH_SHORT).show()
                }
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // 添加动画
        recyclerView.itemAnimator = DefaultItemAnimator()

        swipeRefresh.setOnRefreshListener {
            viewModel.refreshPosts()
        }

        // 设置刷新指示器颜色
        swipeRefresh.setColorSchemeResources(R.color.orange)
    }

    private fun setupScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoadingMore && totalItemCount <= lastVisibleItem + 2) {
                    viewModel.loadMorePosts()
                }
            }
        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                adapter.submitList(state.posts)
                swipeRefresh.isRefreshing = state.isRefreshing
                isLoadingMore = state.isLoadingMore

                // 处理错误状态
                state.error?.let { error ->
                    showErrorDialog(error)
                    viewModel.clearError()
                }
            }
        }
    }

    private fun showErrorDialog(error: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("错误")
            .setMessage(error)
            .setPositiveButton("重试") { _, _ ->
                viewModel.loadPosts()
            }
            .setNegativeButton("取消", null)
            .show()
    }
}