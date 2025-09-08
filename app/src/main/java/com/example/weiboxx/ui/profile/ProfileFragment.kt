package com.example.weiboxx.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weiboxx.R

class ProfileFragment : Fragment() {

    private lateinit var btnPerfectInfo: Button
    private lateinit var topToolbar: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupClickListeners()
    }

    private fun initViews(view: View) {
        btnPerfectInfo = view.findViewById(R.id.btn_perfect_info)
        topToolbar = view.findViewById<LinearLayout>(R.id.top_toolbar) ?: run {
            // 如果没有找到top_toolbar，直接获取顶部的LinearLayout
            view.findViewById<LinearLayout>(android.R.id.content)?.getChildAt(0) as? LinearLayout
                ?: LinearLayout(context)
        }
    }

    private fun setupClickListeners() {
        // 完善资料按钮点击事件
        btnPerfectInfo.setOnClickListener {
            Toast.makeText(context, "完善资料功能", Toast.LENGTH_SHORT).show()
        }

        // 为顶部工具栏的图标添加点击事件
        setupTopToolbarClicks()
        
        // 为统计数据添加点击事件
        setupStatsClicks()
        
        // 为功能入口添加点击事件
        setupFunctionClicks()
    }

    private fun setupTopToolbarClicks() {
        val rootView = view ?: return
        
        // 查找顶部的三个图标并添加点击事件
        val topLayout = rootView.findViewById<LinearLayout>(R.id.top_toolbar) 
            ?: rootView.findViewById<LinearLayout>(android.R.id.content)?.getChildAt(0) as? LinearLayout
        
        topLayout?.let { layout ->
            for (i in 0 until layout.childCount) {
                val child = layout.getChildAt(i)
                if (child is ImageView) {
                    child.setOnClickListener {
                        when (i) {
                            0 -> Toast.makeText(context, "添加好友", Toast.LENGTH_SHORT).show()
                            1 -> Toast.makeText(context, "扫一扫", Toast.LENGTH_SHORT).show()
                            2 -> Toast.makeText(context, "设置", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupStatsClicks() {
        // 这里可以为微博、关注、粉丝数据添加点击事件
        // 由于布局中没有具体的ID，暂时跳过
    }

    private fun setupFunctionClicks() {
        // 这里可以为我的相册等功能入口添加点击事件
        // 由于布局中没有具体的ID，暂时跳过
    }
}