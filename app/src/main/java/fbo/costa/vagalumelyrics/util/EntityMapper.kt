package fbo.costa.vagalumelyrics.util

interface EntityMapper<EntityModel, DomainModel> {
    fun mapFromEntityModel(entityModel: EntityModel): DomainModel
}
