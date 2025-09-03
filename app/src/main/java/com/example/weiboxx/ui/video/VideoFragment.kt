package com.example.weiboxx.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.weiboxx.R
import com.example.weiboxx.database.entity.VideoBean

class VideoFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var videoAdapter: VideoAdapter
    private val videoList = mutableListOf<VideoBean>()

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
        setupViewPager()
    }

    private fun initViews(view: View) {
        viewPager = view.findViewById(R.id.videoViewPager)
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

    private fun setupViewPager() {
        videoAdapter = VideoAdapter(requireContext(), videoList)
        viewPager.adapter = videoAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
    }
}