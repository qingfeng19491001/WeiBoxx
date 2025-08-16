// com/example/weiboxx/ui/discover/adapter/HotRankingAdapter.kt
package com.example.weiboxx.ui.discover.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weiboxx.databinding.ItemHotRankingBinding
import com.example.weiboxx.data.model.HotRanking

class HotRankingAdapter(
    private val onItemClick: (HotRanking) -> Unit
) : ListAdapter<HotRanking, HotRankingAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHotRankingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemHotRankingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ranking: HotRanking) {
            binding.apply {
                tvRank.text = ranking.rank.toString()
                tvTitle.text = ranking.title
                tvSubtitle.text = ranking.subtitle
                tvCategory.text = ranking.category

                root.setOnClickListener {
                    onItemClick(ranking)
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<HotRanking>() {
        override fun areItemsTheSame(oldItem: HotRanking, newItem: HotRanking): Boolean {
            return oldItem.rank == newItem.rank
        }

        override fun areContentsTheSame(oldItem: HotRanking, newItem: HotRanking): Boolean {
            return oldItem == newItem
        }
    }
}
