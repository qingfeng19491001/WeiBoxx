package com.example.weiboxx.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.ImageButton
import android.widget.PopupWindow
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.content.Intent
import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
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
        
        // 设置发布按钮点击事件
        setupPublishButton()
        
        // 设置顶部工具栏按钮点击事件
        setupToolbarButtons()
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
    
    private fun setupToolbarButtons() {
        // 编辑按钮点击事件
        findViewById<android.widget.ImageView>(R.id.iv_edit).setOnClickListener {
            // 跳转到编辑页面或显示编辑功能
            android.widget.Toast.makeText(this, "编辑功能", android.widget.Toast.LENGTH_SHORT).show()
        }
        
        // 红包按钮点击事件
        findViewById<android.widget.ImageView>(R.id.iv_home).setOnClickListener {
            // 显示红包功能
            android.widget.Toast.makeText(this, "红包功能", android.widget.Toast.LENGTH_SHORT).show()
        }
        
        // 添加按钮点击事件
        findViewById<android.widget.ImageView>(R.id.iv_add).setOnClickListener {
            // 显示添加功能菜单
            showAddMenu(it)
        }
    }
    
    private fun showAddMenu(anchorView: android.view.View) {
        // 创建弹窗布局
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.popup_add_menu, null)
        
        // 创建PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        
        // 设置背景
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.popup_background))
        popupWindow.elevation = 8f
        
        // 设置菜单项点击事件
        popupView.findViewById<LinearLayout>(R.id.ll_scan).setOnClickListener {
            android.widget.Toast.makeText(this, "扫一扫", android.widget.Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }
        
        popupView.findViewById<LinearLayout>(R.id.ll_search).setOnClickListener {
            android.widget.Toast.makeText(this, "搜索", android.widget.Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }
        
        popupView.findViewById<LinearLayout>(R.id.ll_message).setOnClickListener {
            // 跳转到消息页面
            selectBottomNavItem(3)
            popupWindow.dismiss()
        }
        
        // 显示弹窗
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)
        popupWindow.showAsDropDown(
            anchorView,
            -popupWindow.contentView.measuredWidth + anchorView.width,
            0
        )
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

        // 创建动画集合
        val animatorSet = AnimatorSet()
        val animators = mutableListOf<ObjectAnimator>()

        when (position) {
            0 -> {
                // 推荐页面
                // 文本颜色动画
                val recommendColorAnimator = ValueAnimator.ofArgb(
                    tvRecommend.currentTextColor,
                    ContextCompat.getColor(this, R.color.orange)
                )
                recommendColorAnimator.addUpdateListener { animator ->
                    tvRecommend.setTextColor(animator.animatedValue as Int)
                }
                
                val followColorAnimator = ValueAnimator.ofArgb(
                    tvFollow.currentTextColor,
                    ContextCompat.getColor(this, android.R.color.darker_gray)
                )
                followColorAnimator.addUpdateListener { animator ->
                    tvFollow.setTextColor(animator.animatedValue as Int)
                }
                
                // 文本大小动画
                val recommendSizeAnimator = ValueAnimator.ofFloat(tvRecommend.textSize, 18f * resources.displayMetrics.scaledDensity)
                recommendSizeAnimator.addUpdateListener { animator ->
                    tvRecommend.textSize = (animator.animatedValue as Float) / resources.displayMetrics.scaledDensity
                }
                
                val followSizeAnimator = ValueAnimator.ofFloat(tvFollow.textSize, 16f * resources.displayMetrics.scaledDensity)
                followSizeAnimator.addUpdateListener { animator ->
                    tvFollow.textSize = (animator.animatedValue as Float) / resources.displayMetrics.scaledDensity
                }
                
                // 指示器透明度动画
                val recommendIndicatorAnimator = ObjectAnimator.ofFloat(indicatorRecommend, "alpha", indicatorRecommend.alpha, 1f)
                val followIndicatorAnimator = ObjectAnimator.ofFloat(indicatorFollow, "alpha", indicatorFollow.alpha, 0f)
                
                // 设置文本加粗
                tvRecommend.paint.isFakeBoldText = true
                tvFollow.paint.isFakeBoldText = false
                
                // 更新指示器可见性
                indicatorRecommend.visibility = View.VISIBLE
                indicatorFollow.visibility = View.VISIBLE
                
                animators.addAll(listOf(
                    recommendColorAnimator as ObjectAnimator,
                    followColorAnimator as ObjectAnimator,
                    recommendSizeAnimator as ObjectAnimator,
                    followSizeAnimator as ObjectAnimator,
                    recommendIndicatorAnimator,
                    followIndicatorAnimator
                ))
            }
            1 -> {
                // 关注页面
                // 文本颜色动画
                val followColorAnimator = ValueAnimator.ofArgb(
                    tvFollow.currentTextColor,
                    ContextCompat.getColor(this, R.color.orange)
                )
                followColorAnimator.addUpdateListener { animator ->
                    tvFollow.setTextColor(animator.animatedValue as Int)
                }
                
                val recommendColorAnimator = ValueAnimator.ofArgb(
                    tvRecommend.currentTextColor,
                    ContextCompat.getColor(this, android.R.color.darker_gray)
                )
                recommendColorAnimator.addUpdateListener { animator ->
                    tvRecommend.setTextColor(animator.animatedValue as Int)
                }
                
                // 文本大小动画
                val followSizeAnimator = ValueAnimator.ofFloat(tvFollow.textSize, 18f * resources.displayMetrics.scaledDensity)
                followSizeAnimator.addUpdateListener { animator ->
                    tvFollow.textSize = (animator.animatedValue as Float) / resources.displayMetrics.scaledDensity
                }
                
                val recommendSizeAnimator = ValueAnimator.ofFloat(tvRecommend.textSize, 16f * resources.displayMetrics.scaledDensity)
                recommendSizeAnimator.addUpdateListener { animator ->
                    tvRecommend.textSize = (animator.animatedValue as Float) / resources.displayMetrics.scaledDensity
                }
                
                // 指示器透明度动画
                val followIndicatorAnimator = ObjectAnimator.ofFloat(indicatorFollow, "alpha", indicatorFollow.alpha, 1f)
                val recommendIndicatorAnimator = ObjectAnimator.ofFloat(indicatorRecommend, "alpha", indicatorRecommend.alpha, 0f)
                
                // 设置文本加粗
                tvFollow.paint.isFakeBoldText = true
                tvRecommend.paint.isFakeBoldText = false
                
                // 更新指示器可见性
                indicatorRecommend.visibility = View.VISIBLE
                indicatorFollow.visibility = View.VISIBLE
                
                animators.addAll(listOf(
                    followColorAnimator as ObjectAnimator,
                    recommendColorAnimator as ObjectAnimator,
                    followSizeAnimator as ObjectAnimator,
                    recommendSizeAnimator as ObjectAnimator,
                    followIndicatorAnimator,
                    recommendIndicatorAnimator
                ))
            }
        }
        
        // 播放动画
        animatorSet.playTogether(animators)
        animatorSet.duration = 200 // 200ms动画时长
        animatorSet.start()
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