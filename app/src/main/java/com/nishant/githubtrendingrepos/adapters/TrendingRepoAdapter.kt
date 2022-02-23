package com.nishant.githubtrendingrepos.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nishant.githubtrendingrepos.databinding.SingleRepoLayoutBinding
import com.nishant.githubtrendingrepos.models.Item

class TrendingRepoAdapter : ListAdapter<Item, TrendingRepoAdapter.TrendingRepo>(DiffUtil()) {
    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean {
            return oldItem == newItem
        }
    }

    class TrendingRepo(private val binding: SingleRepoLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.repo = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingRepo {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SingleRepoLayoutBinding.inflate(inflater, parent, false)
        return TrendingRepo(binding)
    }

    override fun onBindViewHolder(holder: TrendingRepo, position: Int) {
        val currentRepo = currentList[position]
        holder.bind(currentRepo)
    }
}