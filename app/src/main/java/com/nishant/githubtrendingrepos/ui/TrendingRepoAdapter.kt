package com.nishant.githubtrendingrepos.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nishant.githubtrendingrepos.databinding.SingleRepoLayoutBinding
import com.nishant.githubtrendingrepos.ui.viewmodel.TrendingRepository

class TrendingRepoAdapter(
    private val onCheckedCallback: (String, Boolean) -> Unit
) : ListAdapter<TrendingRepository, TrendingRepoAdapter.TrendingRepo>(DiffUtil()) {

    init {
        setHasStableIds(true)
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<TrendingRepository>() {
        override fun areItemsTheSame(
            oldItem: TrendingRepository,
            newItem: TrendingRepository
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: TrendingRepository,
            newItem: TrendingRepository
        ): Boolean {
            return oldItem.isSelected == newItem.isSelected
        }
    }

    inner class TrendingRepo(private val binding: SingleRepoLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TrendingRepository) {
            binding.repo = item
            binding.root.isActivated = item.isSelected
            binding.checkBox.isChecked = item.isSelected

            binding.root.setOnClickListener {
                onCheckedCallback(item.name, !item.isSelected)
            }
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

    override fun getItemId(position: Int): Long = position.toLong()
}