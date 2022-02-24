package com.nishant.githubtrendingrepos.utils

import androidx.recyclerview.widget.DiffUtil
import com.nishant.githubtrendingrepos.data.models.Item

class TrendingReposDiffUtil(
    private val oldList: List<Item>,
    private val newList: List<Item>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].created_at == newList[newItemPosition].created_at
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}