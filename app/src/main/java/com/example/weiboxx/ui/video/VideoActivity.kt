package com.example.weiboxx.ui.video

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import cn.jzvd.Jzvd
import com.example.weiboxx.R
import com.example.weiboxx.database.entity.VideoBean

class VideoActivity : AppCompatActivity(), OnVideoItemClickListener {

    private lateinit var viewPager: ViewPager2
    private lateinit var videoAdapter: VideoAdapter
    private val videoList = mutableListOf<VideoBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        initViews()
        initMockData()
        setupViewPager()
    }

    private fun initViews() {
        viewPager = findViewById(R.id.viewPager)
    }

    private fun initMockData() {

        val mockVideos = listOf(
            VideoBean(
                username = "fangke",
                description = "我的羽毛球在哪里呢？🏸",
                musicName = "访客-创作的原声",
                likeCount = 111000,
                commentCount = 11000,
                shareCount = 1111
            ),
            VideoBean(
                username = "小明",
                description = "今天天气真好，出来晒晒太阳☀️",
                musicName = "经典背景音乐",
                likeCount = 55000,
                commentCount = 3200,
                shareCount = 890
            ),
            VideoBean(
                username = "美食达人",
                description = "教你做简单美味的蛋炒饭🍳",
                musicName = "轻松愉快BGM",
                likeCount = 98000,
                commentCount = 8900,
                shareCount = 2100
            ),
            VideoBean(
                username = "旅行者",
                description = "这里的风景真的太美了！🏞️",
                musicName = "大自然的声音",
                likeCount = 156000,
                commentCount = 15600,
                shareCount = 4500
            ),
            VideoBean(
                username = "健身教练",
                description = "每天10分钟，轻松瘦身💪",
                musicName = "运动节拍",
                likeCount = 203000,
                commentCount = 25000,
                shareCount = 8900
            ),
            VideoBean(
                username = "宠物博主",
                description = "我家小猫咪又调皮了😸",
                musicName = "可爱萌宠音效",
                likeCount = 89000,
                commentCount = 6700,
                shareCount = 1800
            ),
            VideoBean(
                username = "学习达人",
                description = "分享一个超实用的学习方法📚",
                musicName = "专注学习音乐",
                likeCount = 45000,
                commentCount = 3400,
                shareCount = 1200
            ),
            VideoBean(
                username = "游戏玩家",
                description = "这个操作绝了！🎮",
                musicName = "游戏背景音乐",
                likeCount = 187000,
                commentCount = 28000,
                shareCount = 9500
            )
        )

        videoList.addAll(mockVideos)
    }

    private fun setupViewPager() {
        videoAdapter = VideoAdapter(this, videoList)
        videoAdapter.setOnVideoItemClickListener(this)

        viewPager.adapter = videoAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

        // 设置页面切换监听
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 页面切换时暂停所有视频播放
                Jzvd.releaseAllVideos()

            //其他页面切换逻辑
            //showToast("切换到第 ${position + 1} 个视频")
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                //滚动过程中的逻辑
            }
        })
    }

    //实现点击事件接口
    override fun onAvatarClick(position: Int, video: VideoBean) {
        showToast("点击了 ${video.username} 的头像")

        //跳转到用户主页
    }

    override fun onLikeClick(position: Int, video: VideoBean) {
        video.isLiked = !video.isLiked
        if (video.isLiked) {
            video.likeCount += 1
            showToast("点赞成功 ❤️")
        } else {
            video.likeCount -= 1
            showToast("取消点赞")
        }
        videoAdapter.notifyItemChanged(position)
    }

    override fun onCommentClick(position: Int, video: VideoBean) {
        showToast("打开评论区")

        //打开评论界面
    }

    override fun onShareClick(position: Int, video: VideoBean) {
        showToast("分享视频")

        //分享功能
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos()
    }
}