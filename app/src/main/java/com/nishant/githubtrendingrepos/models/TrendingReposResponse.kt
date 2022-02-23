package com.nishant.githubtrendingrepos.models

data class TrendingReposResponse(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)