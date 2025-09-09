package com.example.weiboxx.ui.message

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MessagePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DynamicsFragment.newInstance()
            1 -> MessageListFragment.newInstance()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}