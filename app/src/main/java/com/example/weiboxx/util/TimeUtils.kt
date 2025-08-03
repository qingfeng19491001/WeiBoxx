package com.example.weiboxx.util

object TimeUtils {
    fun formatTimeAgo(timestamp: String): String {
        // 简单的时间格式化逻辑
        return timestamp
    }

    fun formatCount(count: Int): String {
        return when {
            count < 1000 -> count.toString()
            count < 10000 -> "${count / 1000.0}k"
            else -> "${count / 10000.0}万"
        }
    }
}