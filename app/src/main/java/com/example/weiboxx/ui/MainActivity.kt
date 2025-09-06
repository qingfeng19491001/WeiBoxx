package com.example.weiboxx.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.activity.viewModels
import kotlinx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import com.example.weiboxx.R
import com.example.weiboxx.data.repository.PostRepositoryImpl
import com.example.weiboxx.database.AppDatabase
import com.example.weiboxx.network.ApiService
import com.example.weiboxx.ui.base.BaseActivity
import com.example.weiboxx.ui.discover.DiscoverFragment
import com.example.weiboxx.ui.home.follow.FollowFragment
import com.example.weiboxx.ui.home.PostListFragment
import com.example.weiboxx.ui.home.ViewPagerAdapter
import com.example.weiboxx.ui.message.MessageFragment
import com.example.weiboxx.ui.profile.ProfileFragment
import com.example.weiboxx.ui.video.VideoFragment
import com.example.weiboxx.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(PostRepositoryImpl(Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java), 
            Room.databaseBuilder(
                this,
                AppDatabase::class.java,
                "weibo.db"
            ).build().postDao(), 
            this)) }
    private lateinit var viewPager: androidx.viewpager2.widget.ViewPager2

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
        setupBackPressHandler()
    }

    private fun initViewModel() {
        // ViewModel 已通过 viewModels 扩展函数初始化
    }

    private fun initViews() {
        viewPager = findViewById(R.id.viewPager)

        // 设置ViewPager适配器 - 只用于首页的推荐和关注切换
        val fragments = listOf<Fragment>(
            PostListFragment(),
            FollowFragment()
        )
        viewPager.adapter = ViewPagerAdapter(this, fragments)
        // 默认显示关注页面（索引为1）
        viewPager.currentItem = 1
        updateTopNavigation(1)
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
        viewModel.switchBottomNav(index)
        // 首页显示顶部容器，其它页面隐藏
        val homeTop = findViewById<android.view.View>(R.id.home_top_container)
        homeTop.visibility = if (index == 0) android.view.View.VISIBLE else android.view.View.GONE
        
        // 首页时显示ViewPager，其他页面隐藏ViewPager
        viewPager.visibility = if (index == 0) android.view.View.VISIBLE else android.view.View.GONE
        
        if (index == 0) {
            viewModel.switchTab(viewPager.currentItem)
        }
    }

    private fun updateTopNavigation(position: Int) {
        // 更新顶部导航栏UI
        val tvRecommend = findViewById<TextView>(R.id.tv_recommend)
        val tvFollow = findViewById<TextView>(R.id.tv_follow)
        val indicatorRecommend = findViewById<View>(R.id.indicator_recommend)
        val indicatorFollow = findViewById<View>(R.id.indicator_follow)

        when (position) {
            0 -> {
                // 推荐页面
                // 更新文本样式
                tvRecommend.setTextColor(ContextCompat.getColor(this, R.color.orange))
                tvRecommend.textSize = 18f
                tvRecommend.paint.isFakeBoldText = true

                tvFollow.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
                tvFollow.textSize = 16f
                tvFollow.paint.isFakeBoldText = false
                
                // 更新指示器状态
                indicatorRecommend.visibility = View.VISIBLE
                indicatorFollow.visibility = View.INVISIBLE
            }
            1 -> {
                // 关注页面
                // 更新文本样式
                tvFollow.setTextColor(ContextCompat.getColor(this, R.color.orange))
                tvFollow.textSize = 18f
                tvFollow.paint.isFakeBoldText = true

                tvRecommend.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
                tvRecommend.textSize = 16f
                tvRecommend.paint.isFakeBoldText = false
                
                // 更新指示器状态
                indicatorRecommend.visibility = View.INVISIBLE
                indicatorFollow.visibility = View.VISIBLE
            }
        }
    }

    private fun observeViewModel() {
        // 观察 UI 状态
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                // 可以在这里处理 loading、error 等状态
                if (state.error != null) {
                    // 处理错误状态
                    // 比如显示错误提示
                }
            }
        }

        // 观察 Toast 消息
        viewModel.toastMessage.observe(this) { message ->
            if (!message.isNullOrEmpty()) {
                android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
                viewModel.clearToastMessage()
            }
        }

        // 观察当前选中的底部导航
        lifecycleScope.launch {
            viewModel.selectedBottomNavIndex.collect { index ->
                // 如果需要，可以在这里同步 UI 状态
            }
        }

        // 观察当前选中的 tab
        lifecycleScope.launch {
            viewModel.currentTab.collect { tabIndex ->
                if (currentNavIndex == 0) { // 直接使用父类的 protected 属性
                    viewPager.currentItem = tabIndex
                }
            }
        }
    }

    private fun setupBackPressHandler() {
        // 现代化的返回键处理方式
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentNavIndex != 0) { // 直接使用父类的 protected 属性
                    // 如果不在首页，返回首页
                    selectBottomNavItem(0)
                } else {
                    // 在首页则正常退出
                    finish()
                }
            }
        })
    }
}