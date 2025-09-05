package com.example.weiboxx.data.repository

import com.example.weiboxx.data.model.HotTopic
import kotlinx.coroutines.delay

class HotTopicRepository {

    // 模拟网络请求延迟
    private suspend fun simulateNetworkDelay() {
        delay(1000)
    }

    suspend fun getHotTopics(): List<HotTopic> {
        simulateNetworkDelay()
        return listOf(
            HotTopic(1, "韩立结婴", "B站崩了", 1, true, false, 1234567),
            HotTopic(2, "芦昱晓遇见才晓", "微博热搜", 2, false, true, 987654),
            HotTopic(3, "618手机节", "京东大促", 3, true, false, 2345678),
            HotTopic(4, "热点新闻", "社会焦点", 4, false, false, 1876543),
            HotTopic(5, "娱乐八卦", "明星动态", 5, true, true, 3456789),
            HotTopic(6, "科技前沿", "AI发展", 6, false, false, 876543)
        )
    }

    suspend fun getHotQuestions(): List<HotTopic> {
        simulateNetworkDelay()
        return listOf(
            HotTopic(11, "如何学习Kotlin", "MVVM架构", 1, false, false, 234567),
            HotTopic(12, "Android开发", "最佳实践", 2, true, false, 345678),
            HotTopic(13, "RecyclerView", "性能优化", 3, false, true, 456789),
            HotTopic(14, "数据绑定", "ViewBinding", 4, false, false, 567890),
            HotTopic(15, "协程使用", "异步编程", 5, true, false, 678901)
        )
    }

    suspend fun getHotForwards(): List<HotTopic> {
        simulateNetworkDelay()
        return listOf(
            HotTopic(21, "转发抽奖", "iPhone15", 1, true, false, 9876543),
            HotTopic(22, "热门视频", "搞笑段子", 2, false, true, 8765432),
            HotTopic(23, "美食分享", "家常菜谱", 3, false, false, 7654321),
            HotTopic(24, "旅游攻略", "网红打卡", 4, true, false, 6543210),
            HotTopic(25, "健身教程", "减肥方法", 5, false, false, 5432109)
        )
    }

    suspend fun getPublishTopics(): List<HotTopic> {
        simulateNetworkDelay()
        return listOf(
            HotTopic(31, "今日发布", "新功能上线", 1, false, true, 1111111),
            HotTopic(32, "用户投稿", "原创内容", 2, true, false, 2222222),
            HotTopic(33, "官方公告", "重要通知", 3, false, false, 3333333),
            HotTopic(34, "活动预告", "精彩活动", 4, true, true, 4444444),
            HotTopic(35, "版本更新", "Bug修复", 5, false, false, 5555555)
        )
    }

    suspend fun getIndexTopics(): List<HotTopic> {
        simulateNetworkDelay()
        return listOf(
            HotTopic(41, "搜索指数", "热度排行", 1, false, false, 9999999),
            HotTopic(42, "趋势分析", "数据统计", 2, true, false, 8888888),
            HotTopic(43, "用户画像", "行为分析", 3, false, true, 7777777),
            HotTopic(44, "市场洞察", "行业报告", 4, false, false, 6666666),
            HotTopic(45, "竞品分析", "对比研究", 5, true, false, 5555555)
        )
    }

    suspend fun searchTopics(query: String): List<HotTopic> {
        simulateNetworkDelay()
        // 模拟搜索结果
        return getHotTopics().filter {
            it.leftTitle.contains(query) || it.rightTitle.contains(query)
        }
    }
}