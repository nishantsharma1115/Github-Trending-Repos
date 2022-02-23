package com.nishant.githubtrendingrepos.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishant.githubtrendingrepos.models.TrendingReposResponse
import com.nishant.githubtrendingrepos.repository.TrendingRepoRepository
import com.nishant.githubtrendingrepos.utils.Resource
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: TrendingRepoRepository = TrendingRepoRepository()
) : ViewModel() {
    private var _getTrendingReposStatus = MutableLiveData<Resource<TrendingReposResponse>>()
    val getTrendingReposStatus: LiveData<Resource<TrendingReposResponse>> = _getTrendingReposStatus
    fun getTrendingRepos(
        order: String,
        query: String
    ) = viewModelScope.launch {
        _getTrendingReposStatus.postValue(Resource.Loading())
        repository.getTrendingRepos(order, query, onSuccess = {
            _getTrendingReposStatus.postValue(Resource.Success(it))
        }, onFailure = {
            _getTrendingReposStatus.postValue(Resource.Error(it))
        })
    }
}