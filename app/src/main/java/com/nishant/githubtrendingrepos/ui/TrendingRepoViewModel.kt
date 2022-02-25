package com.nishant.githubtrendingrepos.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishant.githubtrendingrepos.data.repository.TrendingRepoRepository
import com.nishant.githubtrendingrepos.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingRepoViewModel @Inject constructor(
    private val repository: TrendingRepoRepository
) : ViewModel() {

    private val queryFlow = MutableStateFlow<String?>(null)

    fun updateQuery(query: String?) = queryFlow.tryEmit(query)

    val trendingRepos = queryFlow.flatMapLatest { query ->
        if (query.isNullOrEmpty()) {
            repository.getAllTrendingRepos()
        } else {
            repository.getSearchedRepos(query)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var _fetchTrendingRepoFromAPIStatus = MutableLiveData<Resource<Unit>>()
    val fetchTrendingRepoFromAPIStatus: LiveData<Resource<Unit>> =
        _fetchTrendingRepoFromAPIStatus

    fun fetchTrendingRepoFromAPI(
        query: String
    ) = viewModelScope.launch {
        _fetchTrendingRepoFromAPIStatus.postValue(Resource.Loading())
        when (val response = repository.fetchTrendingRepoFromAPI(query)) {
            is Resource.Loading -> {
                _fetchTrendingRepoFromAPIStatus.postValue(Resource.Loading())
            }
            else -> {
                _fetchTrendingRepoFromAPIStatus.postValue(response)
            }
        }
    }
}