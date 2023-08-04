package com.rana.data.repository

import com.rana.data.datasource.RemoteDataSource
import com.rana.data.mapper.toRepositoryEntity
import com.rana.data.models.entity.RepositoryItemEntity


class RepositoryGatewayImp constructor(private val remoteDataSource: RemoteDataSource) :
    RepositoryGateway {
    override suspend fun getRepositories(): Result<List<RepositoryItemEntity>> {
        return remoteDataSource.getRepositories().map {
            it.toRepositoryEntity()
        }
    }
}