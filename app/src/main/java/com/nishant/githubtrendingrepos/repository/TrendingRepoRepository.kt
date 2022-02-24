package com.nishant.githubtrendingrepos.repository

import com.nishant.githubtrendingrepos.api.TrendingReposApi
import com.nishant.githubtrendingrepos.room.TrendingRepoDAO
import com.nishant.githubtrendingrepos.room.TrendingRepoEntity
import javax.inject.Inject

class TrendingRepoRepository
@Inject constructor(
    private val trendingRepoDAO: TrendingRepoDAO,
    private val trendingReposApi: TrendingReposApi
) {
    suspend fun fetTrendingRepoFromAPI(
        query: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            val response = trendingReposApi.getTrendingRepos("desc", query)
            response.items.forEach {
                val repo = TrendingRepoEntity(
                    it.full_name,
                    it.stargazers_count,
                    it.language,
                    it.description,
                    it.forks_count
                )
                trendingRepoDAO.insertTrendingRepo(repo)
                onSuccess()
            }
        } catch (e: Exception) {
            onFailure(e.message.toString())
        }
    }

    fun getAllTrendingReposFromRoom() = trendingRepoDAO.getAllTrendingRepos()

    fun getSearchedReposFromRoom(query: String) = trendingRepoDAO.getSearchedRepos(query)
}