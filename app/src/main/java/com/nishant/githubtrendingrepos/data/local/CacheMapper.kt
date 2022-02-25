package com.nishant.githubtrendingrepos.data.local

import com.nishant.githubtrendingrepos.data.models.Item
import javax.inject.Inject

class CacheMapper
@Inject
constructor() {

    private fun mapToEntity(domainModel: Item): TrendingRepoEntity {
        return with(domainModel) {
            TrendingRepoEntity(
                full_name,
                stargazers_count,
                language,
                description,
                forks_count
            )
        }
    }

    fun mapToEntityList(entities: List<Item>): List<TrendingRepoEntity> {
        return entities.map { mapToEntity(it) }
    }
}