package com.nishant.githubtrendingrepos.utils.rvselection

import androidx.recyclerview.selection.ItemKeyProvider
import com.nishant.githubtrendingrepos.adapters.TrendingRepoAdapter

class MyItemKeyProvider(private val adapter: TrendingRepoAdapter) :
    ItemKeyProvider<String>(SCOPE_CACHED) {
    override fun getKey(position: Int): String =
        adapter.currentList[position].full_name

    override fun getPosition(key: String): Int =
        adapter.currentList.indexOfFirst { it.full_name == key }
}