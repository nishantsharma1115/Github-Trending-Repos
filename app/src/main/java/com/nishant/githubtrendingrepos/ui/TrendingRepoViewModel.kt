package com.nishant.githubtrendingrepos.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishant.githubtrendingrepos.repository.TrendingRepoRepository
import com.nishant.githubtrendingrepos.room.TrendingRepoEntity
import com.nishant.githubtrendingrepos.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingRepoViewModel @Inject constructor(
    private val repository: TrendingRepoRepository
) : ViewModel() {
    private var _fetchTrendingRepoFromAPIStatus = MutableLiveData<Resource<Boolean>>()
    val fetchTrendingRepoFromAPIStatus: LiveData<Resource<Boolean>> =
        _fetchTrendingRepoFromAPIStatus

    fun fetchTrendingRepoFromAPI(
        query: String
    ) = viewModelScope.launch {
        _fetchTrendingRepoFromAPIStatus.postValue(Resource.Loading())
        when (val response = repository.fetTrendingRepoFromAPI(query)) {
            is Resource.Loading -> {
                _fetchTrendingRepoFromAPIStatus.postValue(Resource.Success(true))
            }
            is Resource.Success -> {
                _fetchTrendingRepoFromAPIStatus.postValue(Resource.Success(true))
            }
            is Resource.Error -> {
                _fetchTrendingRepoFromAPIStatus.postValue(Resource.Error(response.message.toString()))
            }
        }
    }

    val trendingRepos = repository.getAllTrendingReposFromRoom()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun getSearchedRepos(query: String): Flow<List<TrendingRepoEntity>> {
        return repository.getSearchedReposFromRoom(query)
    }
}