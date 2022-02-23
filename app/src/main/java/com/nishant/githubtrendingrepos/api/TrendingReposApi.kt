package com.nishant.githubtrendingrepos.api

import com.nishant.githubtrendingrepos.models.TrendingReposResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TrendingReposApi {
    @GET("search/repositories")
    suspend fun getTrendingRepos(
        @Query("order") order: String,
        @Query("q") query: String
    ): TrendingReposResponse
}