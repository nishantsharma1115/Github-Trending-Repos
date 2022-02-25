package com.nishant.githubtrendingrepos.data.network

import com.nishant.githubtrendingrepos.data.models.TrendingReposResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TrendingReposApi {

    companion object {
        const val REPO_ENDPOINT = "search/repositories"
    }

    @GET(REPO_ENDPOINT)
    suspend fun getTrendingRepos(
        @Query("order") order: String,
        @Query("q") query: String
    ): TrendingReposResponse
}