package com.example.weiboxx.ui.login

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.example.weiboxx.R

class PasswordLoginActivity: AppCompatActivity(), View.OnClickListener {
    private lateinit var editTextPhoneOrMail: EditText
    private lateinit var password: EditText
    private lateinit var btnLogin: Button

    //用户协议、客服中心
    private lateinit var tvHelp: TextView
    private lateinit var user_agreement: TextView

    //其他登录方式
    private lateinit var ivPhoneLogin: ImageView
    private lateinit var ivWechatLogin: ImageView
    private lateinit var ivQQLogin: ImageView
    private lateinit var ivQrLogin: ImageView

    //用户协议、客服中心网站
    val user_agreement_uri = Uri.parse("https://weibo.com/signup/v5/protocol")
    val customer_service_center_uri=Uri.parse("https://kefu.weibo.com/")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.password_login)
        initViews()
    }

    private fun initViews(){
        editTextPhoneOrMail=findViewById(R.id.editTextPhoneOrMail)
        password=findViewById(R.id.password)
        btnLogin = findViewById(R.id.btnLogin)
        user_agreement=findViewById(R.id.user_agreement)
        tvHelp = findViewById(R.id.tvHelp)
        ivPhoneLogin = findViewById(R.id.ivPhoneLogin)
        ivWechatLogin = findViewById(R.id.ivWechatLogin)
        ivQQLogin = findViewById(R.id.ivQQLogin)
        ivQrLogin = findViewById(R.id.ivQrLogin)

        // 设置点击监听
        btnLogin.setOnClickListener(this)
        user_agreement.setOnClickListener(this)
        tvHelp.setOnClickListener(this)
        ivPhoneLogin.setOnClickListener(this)
        ivWechatLogin.setOnClickListener(this)
        ivQQLogin.setOnClickListener(this)
        ivQrLogin.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when(v.id){
            //用户协议、帮助
            R.id.user_agreement->{
                val builder = CustomTabsIntent.Builder()
                builder.setShowTitle(true)
                val customTabsIntent = builder.build()
                try {
                    customTabsIntent.launchUrl(this, user_agreement_uri)
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, user_agreement_uri))
                }
            }
            R.id.tvHelp->{
                val builder = CustomTabsIntent.Builder()
                builder.setShowTitle(true)
                val customTabsIntent = builder.build()
                try {
                    customTabsIntent.launchUrl(this, customer_service_center_uri)
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, customer_service_center_uri))
                }
            }

            //其他登录方式
            R.id.ivPhoneLogin -> {
                val intent= Intent(this, SmsLoginActivity::class.java)
                startActivity(intent)
            }
            R.id.ivWechatLogin -> {
                Toast.makeText(this, "微信登录", Toast.LENGTH_SHORT).show()
            }
            R.id.ivQQLogin -> {
                Toast.makeText(this, "QQ登录", Toast.LENGTH_SHORT).show()
            }
            R.id.ivQrLogin -> {
                Toast.makeText(this, "二维码登录", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}