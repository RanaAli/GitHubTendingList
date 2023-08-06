package com.rana.data.repository

import com.rana.data.datasource.RemoteDataSource
import com.rana.data.mapper.toRepositoryEntity
import com.rana.domain.entity.RepositoryItemEntity
import com.rana.domain.repository.RepositoryGateway


class RepositoryGatewayImp constructor(private val remoteDataSource: RemoteDataSource) :
    RepositoryGateway {
    override suspend fun getRepositories(): Result<List<RepositoryItemEntity>> {
        return remoteDataSource.getRepositories().map {
            it.toRepositoryEntity()
        }
    }
}