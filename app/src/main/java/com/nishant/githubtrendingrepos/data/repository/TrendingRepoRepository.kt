package com.nishant.githubtrendingrepos.data.repository

import com.nishant.githubtrendingrepos.data.api.TrendingReposApi
import com.nishant.githubtrendingrepos.data.room.CacheMapper
import com.nishant.githubtrendingrepos.data.room.TrendingRepoDAO
import com.nishant.githubtrendingrepos.utils.Resource
import retrofit2.HttpException
import javax.inject.Inject

class TrendingRepoRepository
@Inject constructor(
    private val trendingRepoDAO: TrendingRepoDAO,
    private val trendingReposApi: TrendingReposApi,
    private val cacheMapper: CacheMapper
) {
    suspend fun fetTrendingRepoFromAPI(query: String): Resource<Boolean> = try {
        val response = trendingReposApi.getTrendingRepos("desc", query)
        val localRepos = cacheMapper.mapToEntityList(response.items)
        trendingRepoDAO.insertTrendingRepos(localRepos)
        Resource.Success(true)
    } catch (e: HttpException) {
        Resource.Error(e.code().toString())
    }

    fun getAllTrendingReposFromRoom() = trendingRepoDAO.getAllTrendingRepos()

    fun getSearchedReposFromRoom(query: String) = trendingRepoDAO.getSearchedRepos(query)
}