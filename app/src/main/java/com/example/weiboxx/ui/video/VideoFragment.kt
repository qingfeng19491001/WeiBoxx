package com.example.weiboxx.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import android.widget.FrameLayout
import android.widget.TextView
import com.example.weiboxx.R
import com.example.weiboxx.database.entity.VideoBean

class VideoFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var videoAdapter: VideoAdapter
    private val videoList = mutableListOf<VideoBean>()
    private lateinit var container: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initViews(view)
        initMockData()
        setupRecommend()
        view.findViewById<TextView>(R.id.tv_video_recommend).setOnClickListener { setupRecommend() }
        view.findViewById<TextView>(R.id.tv_video_featured).setOnClickListener { setupFeatured() }
    }

    private fun initViews(view: View) {
        container = view.findViewById(R.id.video_container)
    }

    private fun initMockData() {
        // 添加模拟数据
        for (i in 1..10) {
            val video = VideoBean(
                videoUrl = "https://example.com/video_$i.mp4",
                coverUrl = "https://example.com/cover_$i.jpg",
                username = "作者 $i",
                description = "视频标题 $i",
                musicName = "原创音乐 $i",
                avatarUrl = "https://example.com/avatar_$i.jpg",
                likeCount = i * 1000,
                commentCount = i * 100,
                shareCount = i * 50
            )
            videoList.add(video)
        }
    }

    private fun setupRecommend() {
        viewPager = ViewPager2(requireContext())
        viewPager.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        container.removeAllViews()
        container.addView(viewPager)
        videoAdapter = VideoAdapter(requireContext(), videoList)
        viewPager.adapter = videoAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
    }

    private fun setupFeatured() {
        // 简单复用推荐数据，实际可换为精选源
        setupRecommend()
    }
}