package com.example.weiboxx.ui.discover.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weiboxx.R
import com.example.weiboxx.data.model.HotTopic
import com.example.weiboxx.databinding.ItemHotTopicBinding

class HotTopicAdapter(
    private val onItemClick: (HotTopic) -> Unit
) : ListAdapter<HotTopic, HotTopicAdapter.HotTopicViewHolder>(HotTopicDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotTopicViewHolder {
        val binding = ItemHotTopicBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HotTopicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HotTopicViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HotTopicViewHolder(
        private val binding: ItemHotTopicBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(hotTopic: HotTopic) {
            binding.apply {
                // 根据item_hot_topic.xml的实际布局结构
                // 第一个TextView显示左侧标题
                root.findViewById<android.widget.TextView>(R.id.tv_left_title)?.text = hotTopic.leftTitle

                // 第二个TextView显示右侧标题
                root.findViewById<android.widget.TextView>(R.id.tv_right_title)?.text = hotTopic.rightTitle

                // 设置点击事件
                root.setOnClickListener {
                    onItemClick(hotTopic)
                }
            }
        }
    }

    private class HotTopicDiffCallback : DiffUtil.ItemCallback<HotTopic>() {
        override fun areItemsTheSame(oldItem: HotTopic, newItem: HotTopic): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HotTopic, newItem: HotTopic): Boolean {
            return oldItem == newItem
        }
    }
}