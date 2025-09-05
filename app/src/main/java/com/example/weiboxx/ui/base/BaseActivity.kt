package com.example.weiboxx.ui.base

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.weiboxx.R

/**
 * 基础Activity类，包含底部导航栏的通用实现
 * 所有需要底部导航栏的Activity都应该继承这个类
 */
abstract class BaseActivity : AppCompatActivity() {

    // 底部导航相关
    protected lateinit var navHome: ImageView
    protected lateinit var navPlay: ImageView
    protected lateinit var navSearch: ImageView
    protected lateinit var navMail: ImageView
    protected lateinit var navPerson: ImageView
    protected var currentNavIndex = 0 // 当前选中的导航索引，默认首页

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())

        initBottomNavigation()
        setupBottomNavigation()
    }

    /**
     * 获取布局资源ID
     */
    abstract fun getLayoutResId(): Int

    /**
     * 初始化底部导航栏视图
     */
    private fun initBottomNavigation() {
        // 初始化底部导航视图
        navHome = findViewById(R.id.iv_nav_home)
        navPlay = findViewById(R.id.iv_nav_play)
        navSearch = findViewById(R.id.iv_nav_search)
        navMail = findViewById(R.id.iv_nav_mail)
        navPerson = findViewById(R.id.iv_nav_person)
    }

    /**
     * 设置底部导航栏点击事件
     */
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

    /**
     * 选择底部导航项
     * @param index 导航项索引
     */
    protected fun selectBottomNavItem(index: Int) {
        if (currentNavIndex == index) return // 如果点击的是当前选中项，不做处理

        currentNavIndex = index
        updateBottomNavigationUI()

        // 切换到对应的Fragment
        switchFragment(index)
    }

    /**
     * 更新底部导航栏UI
     */
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

    /**
     * 切换Fragment
     * 子类需要实现这个方法来处理Fragment的切换
     * @param index 要切换到的Fragment索引
     */
    abstract fun switchFragment(index: Int)

    /**
     * 获取当前选中的导航索引
     */
    protected fun getNavIndex(): Int {
        return currentNavIndex
    }
}