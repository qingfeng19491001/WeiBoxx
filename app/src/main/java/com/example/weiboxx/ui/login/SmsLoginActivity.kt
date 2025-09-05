package com.example.weiboxx.ui.login

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager // <<< 確保這個 import 存在
import android.graphics.Color
import android.net.Uri
import android.os.Build
import com.example.weiboxx.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.NotificationCompat
import com.mob.MobSDK
import java.util.Timer
import java.util.TimerTask
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import com.example.weiboxx.ui.MainActivity


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

    companion object {
        private const val CHANNEL_ID = "sms_notification_channel"
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("Permission", "通知权限已授予")
            } else {
                Toast.makeText(this, "你拒绝了通知权限，可能无法及时收到验证码提醒", Toast.LENGTH_SHORT).show()
            }
        }

    private val handler = Handler(Looper.getMainLooper()) { msg ->
        when (msg.what) {
            1 -> {
                val arg = msg.arg1
                if (arg == 1) {
                    btnGetCode.text = "重新获取验证码"
                    count = 60
                    timer?.cancel()
                    btnGetCode.isEnabled = true
                } else {
                    btnGetCode.text = count.toString()
                }
            }
            2 -> {
                Toast.makeText(this@SmsLoginActivity, "获取验证码成功", Toast.LENGTH_LONG).show()
            }
            3 -> {
                Log.i("Code", "获取验证码成功失败")
            }
            4 -> {
                Toast.makeText(this@SmsLoginActivity, "登录成功", Toast.LENGTH_LONG).show()
                val intent= Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
//            5 -> {
//                Toast.makeText(this@SmsLoginActivity, "验证码错误，请重新输入", Toast.LENGTH_LONG).show()
//            }
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smscode_login)

        MobSDK.submitPolicyGrantResult(true)
        askNotificationPermission()
        createNotificationChannel()
        initViews()
        initSmsSDK()
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
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
                when (result) {
                    SMSSDK.RESULT_COMPLETE -> {
                        when (event) {
                            SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE -> {
                                handler.sendMessage(Message.obtain().apply { what = 4 })
                            }
                            SMSSDK.EVENT_GET_VERIFICATION_CODE, SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE -> {
                                handler.sendMessage(Message.obtain().apply { what = 2 })
                            }
                        }
                    }
                    SMSSDK.RESULT_ERROR -> {
                        when (event) {
                            SMSSDK.EVENT_GET_VERIFICATION_CODE -> {
                                handler.sendMessage(Message.obtain().apply { what = 3 })
                            }
                            SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE -> {
                                handler.sendMessage(Message.obtain().apply { what = 5 })
                            }
                        }
                    }
                    else -> {
                        (data as? Throwable)?.printStackTrace()
                    }
                }
            }
        }
        SMSSDK.registerEventHandler(eventHandler)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnGetCode -> getVerificationCode()
            R.id.btnLogin -> login()
            R.id.tvHelp -> openCustomTab(customer_service_center_uri)
            R.id.ivEmailLogin -> startActivity(Intent(this, PasswordLoginActivity::class.java))
            R.id.ivWechatLogin -> Toast.makeText(this, "微信登录", Toast.LENGTH_SHORT).show()
            R.id.ivQQLogin -> Toast.makeText(this, "QQ登录", Toast.LENGTH_SHORT).show()
            R.id.ivQrLogin -> Toast.makeText(this, "二维码登录", Toast.LENGTH_SHORT).show()
            R.id.user_agreement -> openCustomTab(user_agreement_uri)
        }
    }

    private fun openCustomTab(uri: Uri) {
        val builder = CustomTabsIntent.Builder().setShowTitle(true)
        try {
            builder.build().launchUrl(this, uri)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
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
                showPrivacyDialog()
                return
            }
        }
        countdownStart()
        Toast.makeText(this, "验证码666666已发送", Toast.LENGTH_LONG).show()
        showNotification("验证码", "666666", "请勿泄露，点击查看详情")
        // getVerificationCode("86", phone) // 模拟，不用真实短信服务
    }

    @SuppressLint("NotificationPermission")
    private fun showNotification(title: String, text: String, info: String) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(info))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "SmsLogin Channel"
            val descriptionText = "Channel for SMS login verification codes"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showPrivacyDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_privacy_agreement, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create().apply {
                window?.setBackgroundDrawableResource(android.R.color.transparent)
            }

        val btnDisagree = dialogView.findViewById<TextView>(R.id.btn_disagree)
        val btnAgree = dialogView.findViewById<TextView>(R.id.btn_agree)
        val messageText = dialogView.findViewById<TextView>(R.id.message_text)

        setupClickableText(messageText)

        btnDisagree.setOnClickListener { dialog.dismiss() }
        btnAgree.setOnClickListener {
            checkboxAgreement.isChecked = true
            dialog.dismiss()
            val phone = editTextPhone.text.toString().trim()
            if (!TextUtils.isEmpty(phone)) {
                countdownStart()
                getVerificationCode("86", phone)
            }
        }
        dialog.show()
    }

    private fun setupClickableText(textView: TextView) {
        val text = "为保障与你相关的合法权益，请阅读并同意用户协议、隐私条款"
        val spannableString = SpannableString(text)
        val userAgreementSpan = object : ClickableSpan() {
            override fun onClick(widget: View) { openUrl(user_agreement_uri) }
            override fun updateDrawState(ds: TextPaint) {
                ds.color = Color.parseColor("#FF6600")
                ds.isUnderlineText = false
            }
        }
        val privacyPolicySpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val privacyUri = Uri.parse("https://weibo.com/signup/v5/privacy")
                openUrl(privacyUri)
            }
            override fun updateDrawState(ds: TextPaint) {
                ds.color = Color.parseColor("#FF6600")
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(userAgreementSpan, text.indexOf("用户协议"), text.indexOf("用户协议") + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(privacyPolicySpan, text.indexOf("隐私条款"), text.indexOf("隐私条款") + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun openUrl(uri: Uri) {
        val builder = CustomTabsIntent.Builder().setShowTitle(true)
        try {
            builder.build().launchUrl(this, uri)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    private fun login() {
        val phone = editTextPhone.text.toString().trim()
        val code = editTextCode.text.toString().trim()
        when {
            TextUtils.isEmpty(phone) -> Toast.makeText(this, "请输入手机号", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(code) -> Toast.makeText(this, "请输入验证码", Toast.LENGTH_LONG).show()
            !checkboxAgreement.isChecked -> Toast.makeText(this, "请同意用户协议和隐私条款", Toast.LENGTH_LONG).show()
            else -> {
                if (code == "666666") { // 模拟登录
                    Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                submitVerificationCode("86", phone, code) // 真实登录
            }
        }
    }

    private fun getVerificationCode(country: String, phone: String) {
        SMSSDK.getVerificationCode(country, phone)
    }

    private fun getVerificationCode(tempCode: String, country: String, phone: String) {
        SMSSDK.getVerificationCode(tempCode, country, phone)
    }

    private fun submitVerificationCode(country: String, phone: String, code: String) {
        SMSSDK.submitVerificationCode(country, phone, code)
    }

    private fun countdownStart() {
        btnGetCode.isEnabled = false
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                val message = Message.obtain().apply {
                    what = 1
                    arg1 = if (count != 0) --count else 1
                }
                handler.sendMessage(message)
                if (count == 0) return
            }
        }, 1000, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        eventHandler?.let { SMSSDK.unregisterEventHandler(it) }
        timer?.cancel()
    }

}