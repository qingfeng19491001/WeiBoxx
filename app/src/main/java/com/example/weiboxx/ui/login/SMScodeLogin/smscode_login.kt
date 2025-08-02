package com.example.weiboxx.ui.login.SMScodeLogin

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.weiboxx.R

class Smscoded : AppCompatActivity() {

    private lateinit var phoneEditText: EditText
    private lateinit var codeEditText: EditText
    private lateinit var getCodeButton: Button
    private lateinit var loginButton: Button
    private lateinit var countryCodeSpinner: Spinner
    private lateinit var agreeCheckBox: CheckBox
    private lateinit var helpTextView: TextView

    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smscode_login)

        initViews()
        setupSpinner()
        setupListeners()
    }

    private fun initViews() {
        phoneEditText = findViewById(R.id.phoneEditText)
        codeEditText = findViewById(R.id.codeEditText)
        getCodeButton = findViewById(R.id.getCodeButton)
        loginButton = findViewById(R.id.loginButton)
        countryCodeSpinner = findViewById(R.id.countryCodeSpinner)
        agreeCheckBox = findViewById(R.id.agreeCheckBox)
        helpTextView = findViewById(R.id.helpTextView)
    }

    private fun setupSpinner() {
        val countryCode = "+86"
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf(countryCode))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countryCodeSpinner.adapter = adapter
    }

    private fun setupListeners() {
        // 手机号输入监听
        phoneEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateButtonStates()
            }
        })

        // 验证码输入监听
        codeEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateButtonStates()
            }
        })

        // 获取验证码按钮
        getCodeButton.setOnClickListener {
            if (isPhoneNumberValid()) {
                sendVerificationCode()
                startCountDown()
            } else {
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show()
            }
        }

        // 登录按钮
        loginButton.setOnClickListener {
            if (validateInput()) {
                performLogin()
            }
        }

        // 同意协议复选框
        agreeCheckBox.setOnCheckedChangeListener { _, _ ->
            updateButtonStates()
        }

        // 帮助文本点击
        helpTextView.setOnClickListener {
            Toast.makeText(this, "帮助功能", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isPhoneNumberValid(): Boolean {
        val phone = phoneEditText.text.toString().trim()
        return phone.length == 11 && phone.matches(Regex("^1[3-9]\\d{9}$"))
    }

    private fun updateButtonStates() {
        val phoneValid = isPhoneNumberValid()
        val codeEntered = codeEditText.text.toString().trim().length == 6
        val agreed = agreeCheckBox.isChecked

        getCodeButton.isEnabled = phoneValid
        loginButton.isEnabled = phoneValid && codeEntered && agreed

        // 更新登录按钮颜色
        if (loginButton.isEnabled) {
            loginButton.setBackgroundColor(ContextCompat.getColor(this, R.color.orange))
        } else {
            loginButton.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
        }
    }

    private fun sendVerificationCode() {
        // 这里实现发送验证码的逻辑
        Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show()
    }

    private fun startCountDown() {
        getCodeButton.isEnabled = false
        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                getCodeButton.text = "${seconds}秒后重试"
            }

            override fun onFinish() {
                getCodeButton.isEnabled = true
                getCodeButton.text = "获取验证码"
                updateButtonStates()
            }
        }.start()
    }

    private fun validateInput(): Boolean {
        if (!isPhoneNumberValid()) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show()
            return false
        }

        if (codeEditText.text.toString().trim().length != 6) {
            Toast.makeText(this, "请输入6位验证码", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!agreeCheckBox.isChecked) {
            Toast.makeText(this, "请同意用户协议和隐私条款", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun performLogin() {
        // 这里实现登录逻辑
        val phone = phoneEditText.text.toString().trim()
        val code = codeEditText.text.toString().trim()

        // 模拟登录验证
        if (code == "123456") {
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
            // 跳转到主界面等操作
        } else {
            Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}