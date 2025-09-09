package com.example.weiboxx.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weiboxx.R

class MessageListFragment : Fragment() {

    private lateinit var llAtMe: LinearLayout
    private lateinit var llComments: LinearLayout
    private lateinit var llLikes: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupClickListeners()
    }

    private fun initViews(view: View) {
        llAtMe = view.findViewById(R.id.ll_at_me)
        llComments = view.findViewById(R.id.ll_comments)
        llLikes = view.findViewById(R.id.ll_likes)
    }

    private fun setupClickListeners() {
        llAtMe.setOnClickListener {
            Toast.makeText(context, "@我的", Toast.LENGTH_SHORT).show()
        }

        llComments.setOnClickListener {
            Toast.makeText(context, "评论", Toast.LENGTH_SHORT).show()
        }

        llLikes.setOnClickListener {
            Toast.makeText(context, "赞", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance(): MessageListFragment {
            return MessageListFragment()
        }
    }
}