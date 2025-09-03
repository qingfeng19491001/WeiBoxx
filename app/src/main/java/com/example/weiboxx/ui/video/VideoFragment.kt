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
                id = "video_$i",
                title = "视频标题 $i",
                coverUrl = "https://example.com/cover_$i.jpg",
                videoUrl = "https://example.com/video_$i.mp4",
                authorName = "作者 $i",
                likeCount = (i * 1000).toString(),
                commentCount = (i * 100).toString(),
                shareCount = (i * 50).toString()
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