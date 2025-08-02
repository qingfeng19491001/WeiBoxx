package com.example.weiboxx.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import com.example.weiboxx.R
import com.example.weiboxx.data.repository.PostRepositoryImpl
import com.example.weiboxx.database.AppDatabase
import com.example.weiboxx.network.ApiService
import com.example.weiboxx.ui.home.FollowFragment
import com.example.weiboxx.ui.home.PostListFragment
import com.example.weiboxx.ui.home.ViewPagerAdapter
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.weiboxx.ui.publish.WritePostActivity
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var viewPager: ViewPager2

    // 底部导航相关
    private lateinit var navHome: ImageView
    private lateinit var navPlay: ImageView
    private lateinit var navAdd: ImageView
    private lateinit var navSearch: ImageView
    private lateinit var navMail: ImageView
    private lateinit var navPerson: ImageView
    private var currentNavIndex = 0

    private val writePostLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("MainActivity", "WritePost result: ${result.resultCode}")
        if (result.resultCode == RESULT_OK) {
            val content = result.data?.getStringExtra("post_content")
            Log.d("MainActivity", "Post content: $content")
            if (!content.isNullOrEmpty()) {
                Log.d("MainActivity", "Calling viewModel.addPost()")
                viewModel.addPost(content)
            } else {
                Log.w("MainActivity", "Post content is null or empty")
            }
        } else {
            Log.w("MainActivity", "WritePost was cancelled or failed")
        }
    }

    private var publishPopup: PopupWindow? = null

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

        navHome = findViewById(R.id.iv_nav_home)
        navPlay = findViewById(R.id.iv_nav_play)
        navAdd = findViewById(R.id.iv_add)
        navSearch = findViewById(R.id.iv_nav_search)
        navMail = findViewById(R.id.iv_nav_mail)
        navPerson = findViewById(R.id.iv_nav_person)

        val fragments = listOf(
            PostListFragment(),
            FollowFragment()
        )
        viewPager.adapter = ViewPagerAdapter(this, fragments)
    }

    private fun setupTopNavigation() {
        findViewById<TextView>(R.id.tv_recommend).setOnClickListener {
            viewPager.currentItem = 0
            updateTopNavigation(0)
        }

        findViewById<TextView>(R.id.tv_follow).setOnClickListener {
            viewPager.currentItem = 1
            updateTopNavigation(1)
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateTopNavigation(position)
            }
        })
    }

    private fun setupBottomNavigation() {
        navHome.setOnClickListener { selectBottomNavItem(0) }
        navPlay.setOnClickListener { selectBottomNavItem(1) }
        navSearch.setOnClickListener { selectBottomNavItem(2) }
        navMail.setOnClickListener { selectBottomNavItem(3) }
        navPerson.setOnClickListener { selectBottomNavItem(4) }

        // 修复：使用正确的启动方式
        navAdd.setOnClickListener {
            showPublishMenu(it)
        }

        selectBottomNavItem(0)
    }

    private fun selectBottomNavItem(index: Int) {
        if (currentNavIndex == index) return

        currentNavIndex = index
        updateBottomNavigationUI()

        when (index) {
            0 -> {
                Toast.makeText(this, "首页", Toast.LENGTH_SHORT).show()
            }
            1 -> {
                Toast.makeText(this, "视频", Toast.LENGTH_SHORT).show()
            }
            2 -> {
                Toast.makeText(this, "发现", Toast.LENGTH_SHORT).show()
            }
            3 -> {
                Toast.makeText(this, "消息", Toast.LENGTH_SHORT).show()
            }
            4 -> {
                Toast.makeText(this, "我的", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateBottomNavigationUI() {
        val navItems = arrayOf(navHome, navPlay, navSearch, navMail, navPerson)

        for (i in navItems.indices) {
            if (i == currentNavIndex) {
                navItems[i].setColorFilter(ContextCompat.getColor(this, android.R.color.black))
                navItems[i].tag = "selected"
            } else {
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
                tvRecommend.setTextColor(ContextCompat.getColor(this, android.R.color.black))
                tvRecommend.textSize = 18f
                tvRecommend.paint.isFakeBoldText = true

                tvFollow.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
                tvFollow.textSize = 18f
                tvFollow.paint.isFakeBoldText = false
            }
            1 -> {
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
                Log.d("MainActivity", "UI State updated: ${state.posts.size} posts, loading: ${state.isLoading}")
                // 处理状态更新
            }
        }

        // 观察Toast消息
        viewModel.toastMessage.observe(this) { message ->
            Log.d("MainActivity", "Toast message: $message")
            if (message.isNotEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                // 清除消息避免重复显示
                viewModel.clearToastMessage()
            }
        }
    }

    override fun onBackPressed() {
        if (publishPopup?.isShowing == true) {
            publishPopup?.dismiss()
            return
        }

        if (currentNavIndex != 0) {
            selectBottomNavItem(0)
        } else {
            super.onBackPressed()
        }
    }

    private fun showPublishMenu(anchor: View) {
        if (publishPopup?.isShowing == true) {
            return
        }

        val contentView = this.layoutInflater.inflate(R.layout.popup_publish_menu, null, false)

        contentView.findViewById<View>(R.id.ll_write_weibo).setOnClickListener {
            publishPopup?.dismiss()
            // 修复：使用正确的启动方式
            writePostLauncher.launch(Intent(this, WritePostActivity::class.java))
        }

        contentView.findViewById<View>(R.id.ll_album).setOnClickListener {
            Toast.makeText(this, "相册", Toast.LENGTH_SHORT).show()
            publishPopup?.dismiss()
        }

        contentView.findViewById<View>(R.id.ll_check_in).setOnClickListener {
            Toast.makeText(this, "签到/点评", Toast.LENGTH_SHORT).show()
            publishPopup?.dismiss()
        }

        contentView.findViewById<View>(R.id.ll_live).setOnClickListener {
            Toast.makeText(this, "直播", Toast.LENGTH_SHORT).show()
            publishPopup?.dismiss()
        }

        publishPopup = PopupWindow(
            contentView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )

        publishPopup?.animationStyle = R.style.PopupAnimation
        publishPopup?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        publishPopup?.isOutsideTouchable = true

        publishPopup?.setOnDismissListener {
            val window = this.window
            val lp = window.attributes
            lp.alpha = 1.0f
            window.attributes = lp
        }

        val window = this.window
        val lp = window.attributes
        lp.alpha = 0.7f
        window.attributes = lp

        publishPopup?.showAtLocation(anchor.rootView, android.view.Gravity.BOTTOM, 0, 0)
    }
}