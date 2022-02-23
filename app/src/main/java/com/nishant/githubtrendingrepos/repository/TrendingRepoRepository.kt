package com.nishant.githubtrendingrepos.repository

import com.nishant.githubtrendingrepos.api.RetrofitInstance
import com.nishant.githubtrendingrepos.models.TrendingReposResponse

class TrendingRepoRepository {
    suspend fun getTrendingRepos(
        order: String,
        query: String,
        onSuccess: (TrendingReposResponse) -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            val response = RetrofitInstance.api.getTrendingRepos(order, query)
            onSuccess(response)
        } catch (e: Exception) {
            onFailure(e.message.toString())
        }
    }
}