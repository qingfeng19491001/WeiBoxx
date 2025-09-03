package com.example.weiboxx.ui

import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.weiboxx.data.repository.PostRepositoryImpl
import com.example.weiboxx.database.AppDatabase
import com.example.weiboxx.network.ApiService
import com.example.weiboxx.ui.MainViewModel
import com.example.weiboxx.ui.MainViewModelFactory
import com.example.weiboxx.ui.base.BaseActivity
import com.example.weiboxx.ui.discover.DiscoverFragment
import com.example.weiboxx.ui.home.FollowFragment
import com.example.weiboxx.ui.home.PostListFragment
import com.example.weiboxx.ui.home.ViewPagerAdapter
import com.example.weiboxx.ui.message.MessageFragment
import com.example.weiboxx.ui.profile.ProfileFragment
import com.example.weiboxx.ui.video.VideoFragment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.weiboxx.R
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var viewPager: ViewPager2
    
    // 所有Fragment
    private lateinit var homeFragment: PostListFragment
    private lateinit var videoFragment: VideoFragment
    private lateinit var discoverFragment: DiscoverFragment
    private lateinit var messageFragment: MessageFragment
    private lateinit var profileFragment: ProfileFragment
    
    // 当前显示的Fragment
    private var currentFragment: Fragment? = null

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        initViews()
        setupTopNavigation()
        initFragments()
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

        // 设置ViewPager适配器 - 只用于首页的推荐和关注切换
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

    private fun initFragments() {
        // 初始化所有Fragment
        homeFragment = PostListFragment()
        videoFragment = VideoFragment()
        discoverFragment = DiscoverFragment()
        messageFragment = MessageFragment()
        profileFragment = ProfileFragment()
        
        // 默认显示首页
        switchFragment(0)
    }
    
    override fun switchFragment(index: Int) {
        // 根据选中的导航项切换Fragment
        val targetFragment = when (index) {
            0 -> homeFragment
            1 -> videoFragment
            2 -> discoverFragment
            3 -> messageFragment
            4 -> profileFragment
            else -> homeFragment
        }
        
        // 如果当前已经显示该Fragment，不做处理
        if (currentFragment == targetFragment) return
        
        val transaction = supportFragmentManager.beginTransaction()
        
        // 隐藏当前Fragment
        currentFragment?.let {
            transaction.hide(it)
        }
        
        // 如果目标Fragment已添加，则显示；否则添加并显示
        if (targetFragment.isAdded) {
            transaction.show(targetFragment)
        } else {
            transaction.add(R.id.fragment_container, targetFragment)
            transaction.show(targetFragment)
        }
        
        transaction.commit()
        currentFragment = targetFragment
        
        // 更新ViewModel中的状态
        viewModel.setCurrentBottomNav(index)
        if (index == 0) {
            viewModel.setCurrentTab(viewPager.currentItem)
        }
    }

    private fun updateTopNavigation(position: Int) {
        // 更新顶部导航栏UI
        val tvRecommend = findViewById<TextView>(R.id.tv_recommend)
        val tvFollow = findViewById<TextView>(R.id.tv_follow)

        when (position) {
            0 -> {
                // 推荐页面
                tvRecommend.setTextColor(ContextCompat.getColor(this, android.R.color.black))
                tvRecommend.textSize = 18f
                tvRecommend.paint.isFakeBoldText = true

                tvFollow.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
                tvFollow.textSize = 16f
                tvFollow.paint.isFakeBoldText = false
            }
            1 -> {
                // 关注页面
                tvFollow.setTextColor(ContextCompat.getColor(this, android.R.color.black))
                tvFollow.textSize = 18f
                tvFollow.paint.isFakeBoldText = true

                tvRecommend.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
                tvRecommend.textSize = 16f
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