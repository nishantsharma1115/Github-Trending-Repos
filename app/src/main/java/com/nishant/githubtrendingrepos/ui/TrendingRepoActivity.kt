package com.nishant.githubtrendingrepos.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nishant.githubtrendingrepos.R
import com.nishant.githubtrendingrepos.databinding.ActivityMainBinding
import com.nishant.githubtrendingrepos.ui.viewmodel.TrendingRepoViewModel
import com.nishant.githubtrendingrepos.utils.ConnectivityChecker
import com.nishant.githubtrendingrepos.utils.DebouncingQueryTextListener
import com.nishant.githubtrendingrepos.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TrendingRepoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TrendingRepoViewModel by viewModels()
    private lateinit var adapter: TrendingRepoAdapter

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //Retaining the query and selection after process death
        outState.putString("searchedQuery", viewModel.searchedQuery)
        outState.putStringArrayList("selectedRepoList", viewModel.selectedRepoList)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        //Update the selection and query to viewModel after process death
        val searchedQuery = savedInstanceState.getString("searchedQuery")
        val selectedRepoList = savedInstanceState.getStringArrayList("selectedRepoList")
        viewModel.updateQuery(searchedQuery)
        selectedRepoList?.forEach { repoName ->
            viewModel.updateSelection(repoName, true)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.root.requestFocus()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GithubTrendingRepos)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setActionBar(binding.toolbar)

        populateData(savedInstanceState)
        setUpRecyclerView()
        observeSearchViewListener()
        observeTrendingRepoLivedata()

        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.trendingRepos.collect { repos ->
                    adapter.submitList(repos)
                    binding.imgErrorState.visibility = View.GONE
                    binding.edtSearch.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun observeTrendingRepoLivedata() {
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

    private fun observeSearchViewListener() {
        binding.edtSearch.setOnQueryTextListener(
            DebouncingQueryTextListener(lifecycle) { it ->
                viewModel.updateQuery(it.toString())
            }
        )
    }

    private fun setUpRecyclerView() {
        adapter = TrendingRepoAdapter { repoName, isSelected ->
            viewModel.updateSelection(repoName, isSelected)
        }
        binding.rvTrendingRepos.adapter = adapter
        binding.rvTrendingRepos.layoutManager = object : LinearLayoutManager(applicationContext) {
            override fun supportsPredictiveItemAnimations(): Boolean = false
        }
        binding.rvTrendingRepos.setHasFixedSize(false)
    }

    private fun populateData(savedInstanceState: Bundle?) {
        if (ConnectivityChecker.hasInternetConnection(this) && savedInstanceState == null) {
            viewModel.fetchTrendingRepoFromAPI("java")
        } else if (!ConnectivityChecker.hasInternetConnection(this)) {
            if (viewModel.trendingRepos.value.isNullOrEmpty()) {
                binding.edtSearch.visibility = View.GONE
            } else {
                binding.imgErrorState.visibility = View.GONE
                binding.edtSearch.visibility = View.VISIBLE
            }
            showSnackBar("No Internet Connection")
        }
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