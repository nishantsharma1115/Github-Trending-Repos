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
class MainViewModel @Inject constructor(
    private val repository: TrendingRepoRepository
) : ViewModel() {
    private var _fetchTrendingRepoFromAPIStatus = MutableLiveData<Resource<Boolean>>()
    val fetchTrendingRepoFromAPIStatus: LiveData<Resource<Boolean>> =
        _fetchTrendingRepoFromAPIStatus

    fun fetchTrendingRepoFromAPI(
        query: String
    ) = viewModelScope.launch {
        _fetchTrendingRepoFromAPIStatus.postValue(Resource.Loading())
        repository.fetTrendingRepoFromAPI(query, onSuccess = {
            _fetchTrendingRepoFromAPIStatus.postValue(Resource.Success(true))
        }, onFailure = {
            _fetchTrendingRepoFromAPIStatus.postValue(Resource.Error(it))
        })
    }

    val trendingRepos = repository.getAllTrendingReposFromRoom()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun getSearchedRepos(query: String): Flow<List<TrendingRepoEntity>> {
        return repository.getSearchedReposFromRoom(query)
    }
}