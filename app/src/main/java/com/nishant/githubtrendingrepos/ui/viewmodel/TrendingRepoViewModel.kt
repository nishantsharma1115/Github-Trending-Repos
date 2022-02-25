package com.nishant.githubtrendingrepos.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishant.githubtrendingrepos.data.repository.TrendingRepoRepository
import com.nishant.githubtrendingrepos.ui.viewmodel.TrendingRepository.Companion.fromEntity
import com.nishant.githubtrendingrepos.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingRepoViewModel @Inject constructor(
    private val repository: TrendingRepoRepository
) : ViewModel() {

    private val searchedQueryFlow = MutableStateFlow<String?>(null)

    val searchedQuery: String?
        get() = searchedQueryFlow.value

    private val selectedRepoFlow = MutableStateFlow<Set<String>>(HashSet())

    val selectedRepoList: ArrayList<String>
        get() = ArrayList(selectedRepoFlow.value)

    private var _fetchTrendingRepoFromAPIStatus = MutableLiveData<Resource<Unit>>()

    val fetchTrendingRepoFromAPIStatus: LiveData<Resource<Unit>> =
        _fetchTrendingRepoFromAPIStatus

    val trendingRepos = searchedQueryFlow.flatMapLatest { query ->
        if (query.isNullOrEmpty()) {
            repository.getAllTrendingRepos()
        } else {
            repository.getSearchedRepos(query)
        }
    }.combine(selectedRepoFlow) { repo, selectedRepoSet ->
        repo.map { repoEntity ->
            repoEntity.fromEntity(selectedRepoSet.contains(repoEntity.fullName))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateQuery(query: String?) = searchedQueryFlow.tryEmit(query)

    fun updateSelection(repoName: String, isSelected: Boolean) {
        val selections = HashSet<String>()
        selections.addAll(selectedRepoFlow.value)
        if (isSelected) {
            selections.add(repoName)
        } else {
            selections.remove(repoName)
        }
        selectedRepoFlow.value = selections
    }

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