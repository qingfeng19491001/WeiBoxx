package com.example.weiboxx

import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.weiboxx.data.repository.PostRepositoryImpl
import com.example.weiboxx.database.AppDatabase
import com.example.weiboxx.network.ApiService
import com.example.weiboxx.ui.MainViewModel
import com.example.weiboxx.ui.MainViewModelFactory
import com.example.weiboxx.ui.home.FollowFragment
import com.example.weiboxx.ui.home.PostListFragment
import com.example.weiboxx.ui.home.ViewPagerAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var viewPager: ViewPager2

    // 底部导航相关
    private lateinit var navHome: ImageView
    private lateinit var navPlay: ImageView
    private lateinit var navSearch: ImageView
    private lateinit var navMail: ImageView
    private lateinit var navPerson: ImageView
    private var currentNavIndex = 0 // 当前选中的导航索引，默认首页

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModel()
        initViews()
        setupTopNavigation()
        setupBottomNavigation()
        observeViewModel()
    }

    private fun initViewModel() {
        val apiService = Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val postDao = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "weibo.db"
        ).build().postDao()

        val repository = PostRepositoryImpl(apiService, postDao, this)
        val factory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private fun initViews() {
        viewPager = findViewById(R.id.viewPager)

        // 初始化底部导航视图
        navHome = findViewById(R.id.iv_nav_home)
        navPlay = findViewById(R.id.iv_nav_play)
        navSearch = findViewById(R.id.iv_nav_search)
        navMail = findViewById(R.id.iv_nav_mail)
        navPerson = findViewById(R.id.iv_nav_person)

        // 设置ViewPager适配器
        val fragments = listOf(
            PostListFragment(),
            FollowFragment()
        )
        viewPager.adapter = ViewPagerAdapter(this, fragments)
    }

    private fun setupTopNavigation() {
        // 设置顶部导航点击事件
        findViewById<TextView>(R.id.tv_recommend).setOnClickListener {
            viewPager.currentItem = 0
            updateTopNavigation(0)
        }

        findViewById<TextView>(R.id.tv_follow).setOnClickListener {
            viewPager.currentItem = 1
            updateTopNavigation(1)
        }

        // 设置ViewPager页面变化监听
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateTopNavigation(position)
            }
        })
    }

    private fun setupBottomNavigation() {
        // 设置底部导航点击事件
        navHome.setOnClickListener { selectBottomNavItem(0) }
        navPlay.setOnClickListener { selectBottomNavItem(1) }
        navSearch.setOnClickListener { selectBottomNavItem(2) }
        navMail.setOnClickListener { selectBottomNavItem(3) }
        navPerson.setOnClickListener { selectBottomNavItem(4) }

        // 默认选中首页
        selectBottomNavItem(0)
    }

    private fun selectBottomNavItem(index: Int) {
        if (currentNavIndex == index) return // 如果点击的是当前选中项，不做处理

        currentNavIndex = index
        updateBottomNavigationUI()

        // 根据选中的导航项执行对应操作
        when (index) {
            0 -> {
                // 首页 - 显示主要内容
                Toast.makeText(this, "首页", Toast.LENGTH_SHORT).show()
                // 这里可以切换到首页Fragment或重置ViewPager状态
            }
            1 -> {
                // 视频/播放
                Toast.makeText(this, "视频", Toast.LENGTH_SHORT).show()
                // TODO: 启动视频页面或Fragment
            }
            2 -> {
                // 搜索/发现
                Toast.makeText(this, "发现", Toast.LENGTH_SHORT).show()
                // TODO: 启动搜索页面或Fragment
            }
            3 -> {
                // 消息
                Toast.makeText(this, "消息", Toast.LENGTH_SHORT).show()
                // TODO: 启动消息页面或Fragment
            }
            4 -> {
                // 个人中心
                Toast.makeText(this, "我的", Toast.LENGTH_SHORT).show()
                // TODO: 启动个人中心页面或Fragment
            }
        }
    }

    private fun updateBottomNavigationUI() {
        val navItems = arrayOf(navHome, navPlay, navSearch, navMail, navPerson)

        for (i in navItems.indices) {
            if (i == currentNavIndex) {
                // 选中状态：黑色
                navItems[i].setColorFilter(ContextCompat.getColor(this, android.R.color.black))
                navItems[i].tag = "selected"
            } else {
                // 未选中状态：灰色
                navItems[i].setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray))
                navItems[i].tag = "unselected"
            }
        }
    }

    private fun updateTopNavigation(position: Int) {
        val tvRecommend = findViewById<TextView>(R.id.tv_recommend)
        val tvFollow = findViewById<TextView>(R.id.tv_follow)

        when (position) {
            0 -> {
                // 推荐页面
                tvRecommend.setTextColor(ContextCompat.getColor(this, android.R.color.black))
                tvRecommend.textSize = 18f
                tvRecommend.paint.isFakeBoldText = true

                tvFollow.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
                tvFollow.textSize = 18f
                tvFollow.paint.isFakeBoldText = false
            }
            1 -> {
                // 关注页面
                tvFollow.setTextColor(ContextCompat.getColor(this, android.R.color.black))
                tvFollow.textSize = 18f
                tvFollow.paint.isFakeBoldText = true

                tvRecommend.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
                tvRecommend.textSize = 18f
                tvRecommend.paint.isFakeBoldText = false
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                // 处理状态更新
            }
        }
    }

    // 可选：添加返回键处理，防止意外退出
    override fun onBackPressed() {
        if (currentNavIndex != 0) {
            // 如果不在首页，返回首页
            selectBottomNavItem(0)
        } else {
            // 在首页则正常退出
            super.onBackPressed()
        }
    }
}