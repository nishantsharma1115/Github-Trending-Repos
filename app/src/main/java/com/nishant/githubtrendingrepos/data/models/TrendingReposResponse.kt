package com.nishant.githubtrendingrepos.data.models

data class TrendingReposResponse(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)