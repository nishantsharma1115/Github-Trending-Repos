package com.nishant.githubtrendingrepos.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import com.nishant.githubtrendingrepos.adapters.TrendingRepoAdapter
import com.nishant.githubtrendingrepos.databinding.ActivityMainBinding
import com.nishant.githubtrendingrepos.utils.ConnectivityChecker
import com.nishant.githubtrendingrepos.utils.Resource
import com.nishant.githubtrendingrepos.utils.rvselection.MyItemDetailsLookup
import com.nishant.githubtrendingrepos.utils.rvselection.MyItemKeyProvider

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: TrendingRepoAdapter
    private var tracker: SelectionTracker<String>? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val selectedItems = ArrayList<String>()
        tracker?.selection?.forEach { selectedItems.add(it) }
        outState.putStringArrayList("tracker", selectedItems)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val selectedItems = savedInstanceState.getStringArrayList("tracker")
        if (selectedItems != null && selectedItems.size > 0) {
            selectedItems.forEach {
                tracker?.select(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        if (ConnectivityChecker.hasInternetConnection(this) && savedInstanceState == null) {
            viewModel.getTrendingRepos("desc", "java")
        }

        viewModel.getTrendingReposStatus.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val responseData = response.data
                    adapter = TrendingRepoAdapter()
                    binding.rvTrendingRepos.adapter = adapter
                    binding.rvTrendingRepos.layoutManager = LinearLayoutManager(applicationContext)
                    binding.rvTrendingRepos.setHasFixedSize(false)
                    if (responseData != null) {
                        adapter.submitList(responseData.items)
                    } else {
                        adapter.submitList(emptyList())
                    }
                    setSelectionTracker(adapter)
                }
                is Resource.Error -> {

                }
            }
        }
    }

    private fun setSelectionTracker(adapter: TrendingRepoAdapter) {
        tracker = SelectionTracker.Builder<String>(
            "mySelection",
            binding.rvTrendingRepos,
            MyItemKeyProvider(adapter),
            MyItemDetailsLookup(binding.rvTrendingRepos),
            StorageStrategy.createStringStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        adapter.tracker = tracker
    }
}