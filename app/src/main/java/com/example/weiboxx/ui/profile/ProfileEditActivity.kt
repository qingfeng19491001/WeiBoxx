package com.example.weiboxx.ui.profile

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weiboxx.R

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var btnMenu: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)
        
        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btn_back)
        btnMenu = findViewById(R.id.btn_menu)
    }

    private fun setupClickListeners() {
        // 返回按钮点击事件
        btnBack.setOnClickListener {
            finish()
        }

        // 菜单按钮点击事件
        btnMenu.setOnClickListener {
            Toast.makeText(this, "菜单功能", Toast.LENGTH_SHORT).show()
        }

        // 头像挂件点击事件
        findViewById<android.widget.LinearLayout>(R.id.layout_avatar_frame).setOnClickListener {
            Toast.makeText(this, "头像挂件功能", Toast.LENGTH_SHORT).show()
        }

        // 更换头像点击事件
        findViewById<android.widget.LinearLayout>(R.id.layout_change_avatar).setOnClickListener {
            Toast.makeText(this, "更换头像功能", Toast.LENGTH_SHORT).show()
        }

        // 昵称点击事件
        findViewById<android.widget.LinearLayout>(R.id.layout_nickname).setOnClickListener {
            Toast.makeText(this, "修改昵称功能", Toast.LENGTH_SHORT).show()
        }

        // 微博认证点击事件
        findViewById<android.widget.LinearLayout>(R.id.layout_weibo_verify).setOnClickListener {
            Toast.makeText(this, "微博认证功能", Toast.LENGTH_SHORT).show()
        }

        // 简介点击事件
        findViewById<android.widget.LinearLayout>(R.id.layout_intro).setOnClickListener {
            Toast.makeText(this, "修改简介功能", Toast.LENGTH_SHORT).show()
        }

        // 性别点击事件
        findViewById<android.widget.LinearLayout>(R.id.layout_gender).setOnClickListener {
            Toast.makeText(this, "修改性别功能", Toast.LENGTH_SHORT).show()
        }

        // 生日点击事件
        findViewById<android.widget.LinearLayout>(R.id.layout_birthday).setOnClickListener {
            Toast.makeText(this, "修改生日功能", Toast.LENGTH_SHORT).show()
        }

        // 感情状况点击事件
        findViewById<android.widget.LinearLayout>(R.id.layout_relationship).setOnClickListener {
            Toast.makeText(this, "修改感情状况功能", Toast.LENGTH_SHORT).show()
        }

        // 家乡点击事件
        findViewById<android.widget.LinearLayout>(R.id.layout_hometown).setOnClickListener {
            Toast.makeText(this, "修改家乡功能", Toast.LENGTH_SHORT).show()
        }

        // 教育信息点击事件
        findViewById<android.widget.LinearLayout>(R.id.layout_education).setOnClickListener {
            Toast.makeText(this, "添加教育信息功能", Toast.LENGTH_SHORT).show()
        }

        // 工作信息点击事件
        findViewById<android.widget.LinearLayout>(R.id.layout_work).setOnClickListener {
            Toast.makeText(this, "添加工作信息功能", Toast.LENGTH_SHORT).show()
        }

        // 阳光信用点击事件
        findViewById<android.widget.LinearLayout>(R.id.layout_credit).setOnClickListener {
            Toast.makeText(this, "阳光信用功能", Toast.LENGTH_SHORT).show()
        }
    }
}