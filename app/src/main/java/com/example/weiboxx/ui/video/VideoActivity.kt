package com.example.weiboxx.ui.video

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.weiboxx.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class VideoActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        val mainViewPager: ViewPager2 = findViewById(R.id.mainViewPager)
        val tabLayout: TabLayout=findViewById(R.id.tabLayout)

        mainViewPager.apply {
            adapter=object : FragmentStateAdapter(this@VideoActivity){
                override fun getItemCount()=3
                override fun createFragment(position: Int)=when(position){
                    1-> VideoFragment()
                    else -> FooFragment()
                }

            }
            setCurrentItem(1,false)
        }
        TabLayoutMediator(tabLayout,mainViewPager) { tab: TabLayout.Tab, i: Int->
            tab.text=when(i){
                1->"推荐"
                else -> "精选"
            }
        } .attach()
    }
}