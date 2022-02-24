package com.nishant.githubtrendingrepos.data.room

import com.nishant.githubtrendingrepos.data.models.Item
import com.nishant.githubtrendingrepos.utils.EntityMapper
import javax.inject.Inject

class CacheMapper
@Inject
constructor() : EntityMapper<TrendingRepoEntity, Item> {

    override fun mapToEntity(domainModel: Item): TrendingRepoEntity {
        domainModel.also {
            return TrendingRepoEntity(
                it.full_name,
                it.stargazers_count,
                it.language,
                it.description,
                it.forks_count
            )
        }
    }

    fun mapToEntityList(entities: List<Item>): List<TrendingRepoEntity> {
        return entities.map { mapToEntity(it) }
    }
}