package com.nishant.githubtrendingrepos.ui

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nishant.githubtrendingrepos.R
import com.nishant.githubtrendingrepos.adapters.TrendingRepoAdapter
import com.nishant.githubtrendingrepos.data.room.TrendingRepoEntity
import com.nishant.githubtrendingrepos.databinding.ActivityMainBinding
import com.nishant.githubtrendingrepos.utils.ConnectivityChecker
import com.nishant.githubtrendingrepos.utils.DebouncingQueryTextListener
import com.nishant.githubtrendingrepos.utils.Resource
import com.nishant.githubtrendingrepos.utils.rvselection.MyItemDetailsLookup
import com.nishant.githubtrendingrepos.utils.rvselection.MyItemKeyProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TrendingRepoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TrendingRepoViewModel by viewModels()
    private lateinit var adapter: TrendingRepoAdapter
    private var tracker: SelectionTracker<String>? = null
    private var repoList: List<TrendingRepoEntity>? = null

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
        setTheme(R.style.Theme_GithubTrendingRepos)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        setActionBar(binding.toolbar)
        binding.toolbar.title = ""

        if (ConnectivityChecker.hasInternetConnection(this) && savedInstanceState == null) {
            viewModel.fetchTrendingRepoFromAPI("java")
            binding.edtSearch.visibility = View.VISIBLE
        } else if (!ConnectivityChecker.hasInternetConnection(this)) {
            if (repoList.isNullOrEmpty()) {
                binding.edtSearch.visibility = View.GONE
            } else {
                binding.imgErrorState.visibility = View.GONE
                binding.edtSearch.visibility = View.VISIBLE
            }
            showSnackBar("No Internet Connection")
        }

        setUpRV()

        lifecycleScope.launchWhenStarted {
            viewModel.trendingRepos.collect { repos ->
                repoList = repos
                adapter.submitList(repos)
                binding.imgErrorState.visibility = View.GONE
                binding.edtSearch.visibility = View.VISIBLE
            }
        }
        setSelectionTracker(adapter)

        binding.edtSearch.setOnQueryTextListener(
            DebouncingQueryTextListener(lifecycle) { it ->
                if (it.isNullOrEmpty()) {
                    adapter.submitList(repoList)
                } else {
                    lifecycleScope.launchWhenStarted {
                        viewModel.getSearchedRepos(it.toString()).collect { searchRepos ->
                            adapter.submitList(searchRepos)
                        }
                    }
                }
            }
        )

        viewModel.fetchTrendingRepoFromAPIStatus.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.imgErrorState.visibility = View.GONE
                    binding.rvTrendingRepos.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvTrendingRepos.visibility = View.VISIBLE
                    binding.edtSearch.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.rvTrendingRepos.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.imgErrorState.visibility = View.VISIBLE
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

    private fun setUpRV() {
        adapter = TrendingRepoAdapter()
        binding.rvTrendingRepos.adapter = adapter
        binding.rvTrendingRepos.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvTrendingRepos.setHasFixedSize(false)
    }

    private fun showSnackBar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry") {
                if (ConnectivityChecker.hasInternetConnection(this)) {
                    viewModel.fetchTrendingRepoFromAPI("java")
                } else {
                    showSnackBar(msg)
                }
            }
            .setBackgroundTint(resources.getColor(R.color.black))
            .show()
    }
}