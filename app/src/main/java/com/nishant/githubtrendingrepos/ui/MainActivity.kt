package com.nishant.githubtrendingrepos.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nishant.githubtrendingrepos.adapters.TrendingRepoAdapter
import com.nishant.githubtrendingrepos.databinding.ActivityMainBinding
import com.nishant.githubtrendingrepos.utils.ConnectivityChecker
import com.nishant.githubtrendingrepos.utils.Resource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: TrendingRepoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        if (ConnectivityChecker.hasInternetConnection(this)) {
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
                }
                is Resource.Error -> {

                }
            }
        }
    }
}