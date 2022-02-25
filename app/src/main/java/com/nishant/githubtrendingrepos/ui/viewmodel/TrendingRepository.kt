package com.nishant.githubtrendingrepos.ui.viewmodel

import com.nishant.githubtrendingrepos.data.local.TrendingRepoEntity

data class TrendingRepository(
    val name: String,
    val description: String?,
    val language: String?,
    val starCount: Int,
    val forkCount: Int,
    val isSelected: Boolean
) {
    companion object {
        fun TrendingRepoEntity.fromEntity(isSelected: Boolean = false): TrendingRepository {
            return TrendingRepository(
                fullName,
                description,
                language,
                startCount,
                forkCount,
                isSelected
            )
        }
    }
}