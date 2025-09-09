package com.example.weiboxx.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.weiboxx.R

class MessageFragment : Fragment() {

    private lateinit var ivSettings: ImageView
    private lateinit var tvDynamics: TextView
    private lateinit var tvMessages: TextView
    private lateinit var indicatorDynamics: View
    private lateinit var indicatorMessages: View
    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: MessagePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupViewPager()
        setupClickListeners()
    }

    private fun initViews(view: View) {
        ivSettings = view.findViewById(R.id.iv_settings)
        tvDynamics = view.findViewById(R.id.tv_dynamics)
        tvMessages = view.findViewById(R.id.tv_messages)
        indicatorDynamics = view.findViewById(R.id.indicator_dynamics)
        indicatorMessages = view.findViewById(R.id.indicator_messages)
        viewPager = view.findViewById(R.id.view_pager)
    }

    private fun setupViewPager() {
        pagerAdapter = MessagePagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter
        
        // 监听页面切换
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateTabSelection(position)
            }
        })
    }

    private fun setupClickListeners() {
        ivSettings.setOnClickListener {
            Toast.makeText(context, "消息设置", Toast.LENGTH_SHORT).show()
        }

        tvDynamics.setOnClickListener {
            viewPager.currentItem = 0
        }

        tvMessages.setOnClickListener {
            viewPager.currentItem = 1
        }
    }

    private fun updateTabSelection(position: Int) {
        when (position) {
            0 -> {
                // 动态页面
                tvDynamics.setTextColor(resources.getColor(android.R.color.holo_orange_dark, null))
                tvDynamics.textSize = 16f
                tvDynamics.paint.isFakeBoldText = true
                
                tvMessages.setTextColor(resources.getColor(android.R.color.darker_gray, null))
                tvMessages.textSize = 16f
                tvMessages.paint.isFakeBoldText = false
                
                indicatorDynamics.setBackgroundColor(resources.getColor(android.R.color.holo_orange_dark, null))
                indicatorMessages.setBackgroundColor(resources.getColor(android.R.color.transparent, null))
            }
            1 -> {
                // 消息页面
                tvMessages.setTextColor(resources.getColor(android.R.color.holo_orange_dark, null))
                tvMessages.textSize = 16f
                tvMessages.paint.isFakeBoldText = true
                
                tvDynamics.setTextColor(resources.getColor(android.R.color.darker_gray, null))
                tvDynamics.textSize = 16f
                tvDynamics.paint.isFakeBoldText = false
                
                indicatorMessages.setBackgroundColor(resources.getColor(android.R.color.holo_orange_dark, null))
                indicatorDynamics.setBackgroundColor(resources.getColor(android.R.color.transparent, null))
            }
        }
    }
}