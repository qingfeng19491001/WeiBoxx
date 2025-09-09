package com.example.weiboxx.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weiboxx.R

class DynamicsFragment : Fragment() {

    private lateinit var rvDynamics: RecyclerView
    private lateinit var dynamicsAdapter: DynamicsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dynamics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupRecyclerView()
        loadMockData()
    }

    private fun initViews(view: View) {
        rvDynamics = view.findViewById(R.id.rv_dynamics)
    }

    private fun setupRecyclerView() {
        dynamicsAdapter = DynamicsAdapter()
        rvDynamics.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dynamicsAdapter
        }
    }

    private fun loadMockData() {
        val mockDynamics = listOf(
            DynamicItem(
                id = 1,
                username = "多小鹿CAT",
                avatar = "",
                content = "谢谢大家来看上门(((((*ﾟ▽ﾟ*))))) #...",
                time = "昨天",
                hasImage = false,
                hasVideo = false
            ),
            DynamicItem(
                id = 2,
                username = "@电影罗小黑记2",
                avatar = "",
                content = "5(ﾟ！！(((((*ﾟ▽ﾟ*)))))",
                time = "昨天",
                hasImage = false,
                hasVideo = false
            ),
            DynamicItem(
                id = 3,
                username = "王主要Uvin",
                avatar = "",
                content = "弘扬传大众玩偶精神，中华民族，金身前...",
                time = "昨天",
                hasImage = true,
                hasVideo = false
            ),
            DynamicItem(
                id = 4,
                username = "@央视新闻",
                avatar = "",
                content = "【出第一老年群】#九三大闲...",
                time = "9-3",
                hasImage = true,
                hasVideo = false
            ),
            DynamicItem(
                id = 5,
                username = "闲白精彩推荐",
                avatar = "",
                content = "开发者：为宝（中国）软件有限公司",
                time = "8-31",
                hasImage = false,
                hasVideo = true,
                videoDuration = "00:54"
            ),
            DynamicItem(
                id = 6,
                username = "超话❤万圣街超话",
                avatar = "",
                content = "更新了 久违的在微博发一下，连新大画的七夕图",
                time = "8-31",
                hasImage = true,
                hasVideo = false
            )
        )
        
        dynamicsAdapter.updateData(mockDynamics)
    }

    companion object {
        fun newInstance(): DynamicsFragment {
            return DynamicsFragment()
        }
    }
}