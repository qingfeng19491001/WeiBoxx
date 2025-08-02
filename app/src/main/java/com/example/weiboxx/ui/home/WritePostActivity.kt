package com.example.weiboxx.ui.publish

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.weiboxx.R

class WritePostActivity : AppCompatActivity() {

    private lateinit var etPostContent: EditText
    private lateinit var btnPublish: TextView
    private lateinit var ivBack: ImageView
    private lateinit var btnSelectImage: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_post)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        etPostContent = findViewById(R.id.et_post_content)
        btnPublish = findViewById(R.id.btn_publish)
        ivBack = findViewById(R.id.iv_back)
        btnSelectImage = findViewById(R.id.btn_select_image)
    }

    private fun setupListeners() {
        ivBack.setOnClickListener { finish() }

        btnPublish.setOnClickListener {
            val content = etPostContent.text.toString().trim()
            if (content.isEmpty()) {
                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent().apply {
                putExtra("post_content", content)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        btnSelectImage.setOnClickListener {
            Toast.makeText(this, "选择图片功能开发中", Toast.LENGTH_SHORT).show()
        }
    }
}