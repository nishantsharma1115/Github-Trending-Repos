package com.nishant.githubtrendingrepos.utils

interface EntityMapper<Entity, DomainModel> {

    fun mapToEntity(domainModel: DomainModel): Entity
}