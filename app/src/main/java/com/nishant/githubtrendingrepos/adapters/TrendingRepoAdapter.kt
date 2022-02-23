package com.nishant.githubtrendingrepos.adapters

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nishant.githubtrendingrepos.databinding.SingleRepoLayoutBinding
import com.nishant.githubtrendingrepos.models.Item

class TrendingRepoAdapter : ListAdapter<Item, TrendingRepoAdapter.TrendingRepo>(DiffUtil()) {

    var tracker: SelectionTracker<String>? = null

    init {
        setHasStableIds(true)
    }

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

    inner class TrendingRepo(private val binding: SingleRepoLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item, isActivated: Boolean = false) {
            binding.repo = item
            binding.root.isActivated = isActivated
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<String> =
            object : ItemDetailsLookup.ItemDetails<String>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): String = getItem(adapterPosition).full_name
                override fun inSelectionHotspot(e: MotionEvent): Boolean = true
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingRepo {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SingleRepoLayoutBinding.inflate(inflater, parent, false)
        return TrendingRepo(binding)
    }

    override fun onBindViewHolder(holder: TrendingRepo, position: Int) {
        val currentRepo = currentList[position]
        tracker?.let {
            holder.bind(currentRepo, it.isSelected(currentRepo.full_name))
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()
}