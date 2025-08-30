package com.example.weiboxx.ui.login

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import com.example.weiboxx.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.browser.customtabs.CustomTabsIntent
import com.mob.MobSDK
import java.util.Timer
import java.util.TimerTask
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK

class SmsLoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var editTextPhone: EditText
    private lateinit var editTextCode: EditText
    private lateinit var btnGetCode: Button
    private lateinit var btnLogin: Button
    private lateinit var checkboxAgreement: CheckBox
    private lateinit var tvHelp: TextView
    private lateinit var user_agreement: TextView

    //其他登录方式
    private lateinit var ivEmailLogin: ImageView
    private lateinit var ivWechatLogin: ImageView
    private lateinit var ivQQLogin: ImageView
    private lateinit var ivQrLogin: ImageView

    private var eventHandler: EventHandler? = null
    private var timer: Timer? = null
    private var count = 60

    val user_agreement_uri = Uri.parse("https://weibo.com/signup/v5/protocol")
    val customer_service_center_uri=Uri.parse("https://kefu.weibo.com/")

    private val handler = Handler(Looper.getMainLooper()) { msg ->
        when (msg.what) {
            1 -> {
                val arg = msg.arg1
                if (arg == 1) {
                    btnGetCode.text = "重新获取"
                    // 计时结束停止计时把值恢复
                    count = 60
                    timer?.cancel()
                    btnGetCode.isEnabled = true
                } else {
                    btnGetCode.text = count.toString()
                }
            }
            2 -> {
                Toast.makeText(this@SmsLoginActivity, "获取短信验证码成功", Toast.LENGTH_LONG).show()
            }
            3 -> {
                Log.i("Code", "获取短信验证码失败")
                Toast.makeText(this@SmsLoginActivity, "获取短信验证码失败", Toast.LENGTH_LONG).show()
            }
            4 -> {
                Toast.makeText(this@SmsLoginActivity, "登录成功", Toast.LENGTH_LONG).show()
                // 处理登录成功逻辑，比如跳转到主界面
                finish()
            }
            5 -> {
                Toast.makeText(this@SmsLoginActivity, "验证码错误，请重新输入", Toast.LENGTH_LONG).show()
            }
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smscode_login)

        // 提交隐私协议授权结果
        MobSDK.submitPolicyGrantResult(true)

        initViews()
        initSmsSDK()
    }

    private fun initViews() {
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextCode = findViewById(R.id.editTextCode)
        btnGetCode = findViewById(R.id.btnGetCode)
        btnLogin = findViewById(R.id.btnLogin)
        checkboxAgreement = findViewById(R.id.checkboxAgreement)
        tvHelp = findViewById(R.id.tvHelp)

        ivEmailLogin = findViewById(R.id.ivEmailLogin)
        ivWechatLogin = findViewById(R.id.ivWechatLogin)
        ivQQLogin = findViewById(R.id.ivQQLogin)
        ivQrLogin = findViewById(R.id.ivQrLogin)

        user_agreement=findViewById(R.id.user_agreement)

        // 设置点击监听
        btnGetCode.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
        tvHelp.setOnClickListener(this)
        ivEmailLogin.setOnClickListener(this)
        ivWechatLogin.setOnClickListener(this)
        ivQQLogin.setOnClickListener(this)
        ivQrLogin.setOnClickListener(this)
        user_agreement.setOnClickListener(this)

    }

    private fun initSmsSDK() {
        eventHandler = object : EventHandler() {
            override fun afterEvent(event: Int, result: Int, data: Any?) {
                // TODO 此处为子线程！不可直接处理UI线程！处理后续操作需传到主线程中操作！
                when (result) {
                    SMSSDK.RESULT_COMPLETE -> {
                        // 成功回调
                        when (event) {
                            SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE -> {
                                // 提交短信、语音验证码成功
                                val message = Message.obtain().apply {
                                    what = 4
                                }
                                handler.sendMessage(message)
                            }
                            SMSSDK.EVENT_GET_VERIFICATION_CODE -> {
                                val message = Message.obtain().apply {
                                    what = 2
                                }
                                handler.sendMessage(message)
                            }
                            SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE -> {
                                // 获取语音验证码成功
                                val message = Message.obtain().apply {
                                    what = 2
                                }
                                handler.sendMessage(message)
                            }
                        }
                    }
                    SMSSDK.RESULT_ERROR -> {
                        // 失败回调
                        when (event) {
                            SMSSDK.EVENT_GET_VERIFICATION_CODE -> {
                                val message = Message.obtain().apply {
                                    what = 3
                                }
                                handler.sendMessage(message)
                            }
                            SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE -> {
                                val message = Message.obtain().apply {
                                    what = 5
                                }
                                handler.sendMessage(message)
                            }
                        }
                    }
                    else -> {
                        // 其他失败回调
                        (data as? Throwable)?.printStackTrace()
                    }
                }
            }
        }
        SMSSDK.registerEventHandler(eventHandler) // 注册短信回调
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnGetCode -> {
                getVerificationCode()
            }
            R.id.btnLogin -> {
                login()
            }
            R.id.tvHelp->{
//                startActivity(Intent(Intent.ACTION_VIEW, customer_service_center_uri))
                val builder = CustomTabsIntent.Builder()
                builder.setShowTitle(true)
                // 尝试使用阅读模式
                val customTabsIntent = builder.build()
                try {
                    customTabsIntent.launchUrl(this, customer_service_center_uri)
                } catch (e: ActivityNotFoundException) {
                    // 如果没有支持的浏览器，使用默认方式
                    startActivity(Intent(Intent.ACTION_VIEW, customer_service_center_uri))
                }
            }
            R.id.ivEmailLogin -> {
                val intent= Intent(this, PasswordLoginActivity::class.java)
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
            R.id.user_agreement->{
//                startActivity(Intent(Intent.ACTION_VIEW, user_agreement_uri))
                val builder = CustomTabsIntent.Builder()
                builder.setShowTitle(true)

                // 尝试使用阅读模式
                val customTabsIntent = builder.build()
                try {
                    customTabsIntent.launchUrl(this, user_agreement_uri)
                } catch (e: ActivityNotFoundException) {
                    // 如果没有支持的浏览器，使用默认方式
                    startActivity(Intent(Intent.ACTION_VIEW, user_agreement_uri))
                }
            }

        }
    }

    private fun getVerificationCode() {
        val phone = editTextPhone.text.toString().trim()

        when {
            TextUtils.isEmpty(phone) -> {
                Toast.makeText(this, "请输入手机号码", Toast.LENGTH_LONG).show()
                return
            }
            !checkboxAgreement.isChecked -> {
                Toast.makeText(this, "请同意用户协议和隐私条款", Toast.LENGTH_LONG).show()
                return
            }
        }

        // 开始倒计时
        countdownStart()

        // 获取验证码
        getVerificationCode("86", phone)
    }

    private fun login() {
        val phone = editTextPhone.text.toString().trim()
        val code = editTextCode.text.toString().trim()

        when {
            TextUtils.isEmpty(phone) -> {
                Toast.makeText(this, "请输入手机号码", Toast.LENGTH_LONG).show()
                return
            }
            TextUtils.isEmpty(code) -> {
                Toast.makeText(this, "请输入验证码", Toast.LENGTH_LONG).show()
                return
            }
            !checkboxAgreement.isChecked -> {
                Toast.makeText(this, "请同意用户协议和隐私条款", Toast.LENGTH_LONG).show()
                return
            }
        }

        // 提交验证码
        submitVerificationCode("86", phone, code)
    }

    /**
     * 请求文本验证码
     * @param country 国家区号
     * @param phone 手机号
     */
    private fun getVerificationCode(country: String, phone: String) {
        // 获取短信验证码
        SMSSDK.getVerificationCode(country, phone)
    }

    /**
     * 请求文本验证码(带模板编号)
     * @param tempCode 模板编号
     * @param country 国家区号
     * @param phone 手机号
     */
    private fun getVerificationCode(tempCode: String, country: String, phone: String) {
        // 获取短信验证码
        SMSSDK.getVerificationCode(tempCode, country, phone)
    }

    /**
     * 提交验证码
     * @param country 国家区号
     * @param phone 手机号
     * @param code 验证码
     */
    private fun submitVerificationCode(country: String, phone: String, code: String) {
        SMSSDK.submitVerificationCode(country, phone, code)
    }

    // 倒计时函数
    private fun countdownStart() {
        btnGetCode.isEnabled = false
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                val message = Message.obtain().apply {
                    what = 1
                    arg1 = count
                }
                if (count != 0) {
                    count--
                    handler.sendMessage(message)
                } else {
                    message.arg1 = 1
                    handler.sendMessage(message)
                    return
                }
            }
        }, 1000, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 使用完EventHandler需注销，否则可能出现内存泄漏
        eventHandler?.let {
            SMSSDK.unregisterEventHandler(it)
        }
        timer?.cancel()
    }
}