package com.nishant.githubtrendingrepos.data.repository

import com.nishant.githubtrendingrepos.data.local.CacheMapper
import com.nishant.githubtrendingrepos.data.local.TrendingRepoDAO
import com.nishant.githubtrendingrepos.data.network.TrendingReposApi
import com.nishant.githubtrendingrepos.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class TrendingRepoRepository
@Inject constructor(
    private val trendingRepoDAO: TrendingRepoDAO,
    private val trendingReposApi: TrendingReposApi,
    private val cacheMapper: CacheMapper
) {
    suspend fun fetchTrendingRepoFromAPI(query: String): Resource<Unit> = try {
        withContext(Dispatchers.IO) {
            val response = trendingReposApi.getTrendingRepos("desc", query)
            val localRepos = cacheMapper.mapToEntityList(response.items)
            trendingRepoDAO.insertTrendingRepos(localRepos)
            Resource.Success(Unit)
        }
    } catch (e: HttpException) {
        Resource.Error(e.code().toString())
    }

    fun getAllTrendingRepos() = trendingRepoDAO.getAllTrendingRepos()
        .flowOn(Dispatchers.IO)

    fun getSearchedRepos(query: String) =
        trendingRepoDAO.getSearchedRepos(query).flowOn(Dispatchers.IO)
}