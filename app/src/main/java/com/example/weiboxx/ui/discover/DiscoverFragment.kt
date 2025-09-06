package com.example.weiboxx.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weiboxx.R
import com.example.weiboxx.databinding.FragmentDiscoverBinding
import com.example.weiboxx.ui.discover.adapter.HotTopicAdapter

class DiscoverFragment : Fragment() {

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!

    // 使用 viewModels() 扩展函数
    private val viewModel: DiscoverViewModel by viewModels()
    private lateinit var hotTopicAdapter: HotTopicAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)

        // ViewModel 已通过 viewModels() 扩展函数初始化

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()

        // 加载数据
        viewModel.loadHotTopics()
    }

    private fun setupRecyclerView() {
        hotTopicAdapter = HotTopicAdapter { hotTopic ->
            // 处理热搜点击事件
            viewModel.onHotTopicClicked(hotTopic)
        }

        // 如果您的布局中有RecyclerView，请使用以下代码
        // binding.recyclerViewHotTopics.apply {
        //     adapter = hotTopicAdapter
        //     layoutManager = LinearLayoutManager(context)
        // }
    }

    private fun setupClickListeners() {
        // 搜索按钮点击
        binding.tvSearch.setOnClickListener {
            val searchQuery = "芦昱晓遇见才晓" // 从搜索框获取文本
            viewModel.onSearchClicked(searchQuery)
        }

        // 标签页点击事件
        binding.tabHot.setOnClickListener {
            viewModel.onTabSelected(TabType.HOT)
            updateTabSelection(TabType.HOT)
        }

        binding.tabHotQuestion.setOnClickListener {
            viewModel.onTabSelected(TabType.HOT_QUESTION)
            updateTabSelection(TabType.HOT_QUESTION)
        }

        binding.tabHotForward.setOnClickListener {
            viewModel.onTabSelected(TabType.HOT_FORWARD)
            updateTabSelection(TabType.HOT_FORWARD)
        }

        binding.tabPublish.setOnClickListener {
            viewModel.onTabSelected(TabType.PUBLISH)
            updateTabSelection(TabType.PUBLISH)
        }

        binding.tabIndex.setOnClickListener {
            viewModel.onTabSelected(TabType.INDEX)
            updateTabSelection(TabType.INDEX)
        }
    }

    private fun observeViewModel() {
        // 观察热搜数据
        viewModel.hotTopics.observe(viewLifecycleOwner, Observer { hotTopics ->
            hotTopicAdapter.submitList(hotTopics)
        })

        // 观察加载状态
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            // 显示/隐藏加载指示器
            // binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        // 观察错误信息
        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {
                // 显示错误信息
                // Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

        // 观察当前选中的标签
        viewModel.selectedTab.observe(viewLifecycleOwner, Observer { tabType ->
            updateTabSelection(tabType)
        })
    }

    private fun updateTabSelection(selectedTab: TabType) {
        // 重置所有标签样式
        resetTabStyles()

        // 设置选中标签样式
        when (selectedTab) {
            TabType.HOT -> {
                binding.tabHot.setTextColor(resources.getColor(R.color.orange, null))
                binding.tabHot.setBackgroundResource(R.drawable.selected_tab_bg)
            }
            TabType.HOT_QUESTION -> {
                binding.tabHotQuestion.setTextColor(resources.getColor(R.color.orange, null))
                binding.tabHotQuestion.setBackgroundResource(R.drawable.selected_tab_bg)
            }
            TabType.HOT_FORWARD -> {
                binding.tabHotForward.setTextColor(resources.getColor(R.color.orange, null))
                binding.tabHotForward.setBackgroundResource(R.drawable.selected_tab_bg)
            }
            TabType.PUBLISH -> {
                binding.tabPublish.setTextColor(resources.getColor(R.color.orange, null))
                binding.tabPublish.setBackgroundResource(R.drawable.selected_tab_bg)
            }
            TabType.INDEX -> {
                binding.tabIndex.setTextColor(resources.getColor(R.color.orange, null))
                binding.tabIndex.setBackgroundResource(R.drawable.selected_tab_bg)
            }
        }
    }

    private fun resetTabStyles() {
        val grayColor = resources.getColor(R.color.gray_666, null)

        binding.tabHot.setTextColor(grayColor)
        binding.tabHot.background = null

        binding.tabHotQuestion.setTextColor(grayColor)
        binding.tabHotQuestion.background = null

        binding.tabHotForward.setTextColor(grayColor)
        binding.tabHotForward.background = null

        binding.tabPublish.setTextColor(grayColor)
        binding.tabPublish.background = null

        binding.tabIndex.setTextColor(grayColor)
        binding.tabIndex.background = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

enum class TabType {
    HOT, HOT_QUESTION, HOT_FORWARD, PUBLISH, INDEX
}