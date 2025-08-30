package com.example.weiboxx.ui.mine

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.weiboxx.R

class CenterActivity : AppCompatActivity() {

    private lateinit var tvUsername: TextView   // 显示用户名的TextView
    private lateinit var tvIntro: TextView     // 显示简介的TextView
    private lateinit var btnPerfectInfo: Button// “完善资料”按钮

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置布局（替换为你的布局文件名，如R.layout.activity_center）
        setContentView(R.layout.personal_center)

        // 初始化控件
        tvUsername = findViewById(R.id.tv_username)
        tvIntro = findViewById(R.id.tv_intro)
        btnPerfectInfo = findViewById(R.id.btn_perfect_info)

        // 给“完善资料”按钮设置点击事件
        btnPerfectInfo.setOnClickListener {
            showEditInfoDialog()
        }
    }

    /** 弹出“修改资料”的对话框 */
    private fun showEditInfoDialog() {
        // 加载对话框布局
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_info, null)
        val etNewUsername = dialogView.findViewById<EditText>(R.id.et_new_username)
        val etNewIntro = dialogView.findViewById<EditText>(R.id.et_new_intro)

        // 初始化输入框内容（显示当前用户名和简介）
        etNewUsername.setText(tvUsername.text.toString())
        val currentIntro = tvIntro.text.toString().replace("简介：", "") // 去掉“简介：”前缀
        etNewIntro.setText(currentIntro)

        // 构建并显示AlertDialog
        AlertDialog.Builder(this)
            .setTitle("完善资料")           // 对话框标题
            .setView(dialogView)           // 设置自定义布局
            .setPositiveButton("确定") { _, _ ->
                // 点击“确定”时，获取输入内容并更新TextView
                val newUsername = etNewUsername.text.toString().trim()
                val newIntro = etNewIntro.text.toString().trim()

                // 更新用户名（非空时才更新）
                if (newUsername.isNotEmpty()) {
                    tvUsername.text = newUsername
                }
                // 更新简介（空则显示“暂无简介”）
                tvIntro.text = "简介：${if (newIntro.isNotEmpty()) newIntro else "暂无简介"}"
            }
            .setNegativeButton("取消", null) // 点击“取消”不做操作
            .show()
    }
}