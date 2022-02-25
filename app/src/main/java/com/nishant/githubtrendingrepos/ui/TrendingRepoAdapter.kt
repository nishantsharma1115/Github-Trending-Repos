package com.nishant.githubtrendingrepos.ui

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nishant.githubtrendingrepos.databinding.SingleRepoLayoutBinding
import com.nishant.githubtrendingrepos.data.local.TrendingRepoEntity

class TrendingRepoAdapter :
    ListAdapter<TrendingRepoEntity, TrendingRepoAdapter.TrendingRepo>(DiffUtil()) {

    var tracker: SelectionTracker<String>? = null

    init {
        setHasStableIds(true)
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<TrendingRepoEntity>() {
        override fun areItemsTheSame(
            oldItem: TrendingRepoEntity,
            newItem: TrendingRepoEntity
        ): Boolean {
            return oldItem.fullName == newItem.fullName
        }

        override fun areContentsTheSame(
            oldItem: TrendingRepoEntity,
            newItem: TrendingRepoEntity
        ): Boolean {
            return false
        }
    }

    inner class TrendingRepo(private val binding: SingleRepoLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TrendingRepoEntity, isActivated: Boolean = false) {
            binding.repo = item
            binding.root.isActivated = isActivated
            binding.checkBox.isChecked = isActivated
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<String> =
            object : ItemDetailsLookup.ItemDetails<String>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): String = getItem(adapterPosition).fullName
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
            holder.bind(currentRepo, it.isSelected(currentRepo.fullName))
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()
}