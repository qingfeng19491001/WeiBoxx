package com.example.weiboxx.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weiboxx.R

class MessageFragment : Fragment() {

    private lateinit var ivSettings: ImageView

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
        setupClickListeners()
    }

    private fun initViews(view: View) {
        ivSettings = view.findViewById(R.id.iv_settings)
    }

    private fun setupClickListeners() {
        ivSettings.setOnClickListener {
            Toast.makeText(context, "消息设置", Toast.LENGTH_SHORT).show()
        }
    }
}