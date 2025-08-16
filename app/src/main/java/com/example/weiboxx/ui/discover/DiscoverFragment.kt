package com.example.weiboxx.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weiboxx.R
import com.example.weiboxx.databinding.FragmentDiscoverBinding
import com.example.weiboxx.data.model.HotTopic
import com.example.weiboxx.ui.discover.adapter.HotRankingAdapter
import com.example.weiboxx.ui.discover.adapter.PostImagesAdapter

class DiscoverFragment : Fragment() {

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DiscoverViewModel
    private lateinit var hotRankingAdapter: HotRankingAdapter
    private lateinit var postImagesAdapter: PostImagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerViews()
        setupClickListeners()
        observeData()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[DiscoverViewModel::class.java]
    }

    private fun setupRecyclerViews() {
        // Hot Ranking RecyclerView
        hotRankingAdapter = HotRankingAdapter { ranking ->
            viewModel.onHotTopicClicked(
                HotTopic(
                    id = ranking.rank,
                    title = ranking.title,
                    rank = ranking.rank
                )
            )
        }

//        binding.rvHotRanking.apply {
//            adapter = hotRankingAdapter
//            layoutManager = LinearLayoutManager(context)
//        }

        // Post Images RecyclerView
        postImagesAdapter = PostImagesAdapter { imageUrl ->
            // Handle image click
        }

//        binding.rvPostImages.apply {
//            adapter = postImagesAdapter
//            layoutManager = GridLayoutManager(context, 3)
//        }
    }

    private fun setupClickListeners() {
        // Search click
        binding.tvSearch.setOnClickListener {
            viewModel.onSearchClicked()
        }

        // Tab clicks
        binding.tabHot.setOnClickListener {
            selectTab(0)
            viewModel.onTabSelected(0)
        }

        binding.tabHotQuestion.setOnClickListener {
            selectTab(1)
            viewModel.onTabSelected(1)
        }

        binding.tabHotForward.setOnClickListener {
            selectTab(2)
            viewModel.onTabSelected(2)
        }

        binding.tabPublish.setOnClickListener {
            selectTab(3)
            viewModel.onTabSelected(3)
        }

        binding.tabIndex.setOnClickListener {
            selectTab(4)
            viewModel.onTabSelected(4)
        }
    }

    private fun selectTab(selectedIndex: Int) {
        val tabs = listOf(
            binding.tabHot,
            binding.tabHotQuestion,
            binding.tabHotForward,
            binding.tabPublish,
            binding.tabIndex
        )

        tabs.forEachIndexed { index, tab ->
            if (index == selectedIndex) {
                tab.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))
                tab.setBackgroundResource(R.drawable.selected_tab_bg)
            } else {
                tab.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_666))
                tab.background = null
            }
        }
    }

    private fun observeData() {
        viewModel.hotRankings.observe(viewLifecycleOwner) { rankings ->
            hotRankingAdapter.submitList(rankings)
        }

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            if (posts.isNotEmpty()) {
                val post = posts[0]
//                postImagesAdapter.submitList(post.images)
            }
        }

        viewModel.selectedTab.observe(viewLifecycleOwner) { tabIndex ->
            selectTab(tabIndex)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Handle loading state if needed
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}