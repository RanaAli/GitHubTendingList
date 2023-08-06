package com.rana.data.repository

import com.rana.data.datasource.RemoteDataSource
import com.rana.data.mapper.toRepositoryEntity
import com.rana.domain.entity.RepositoryItemEntity
import com.rana.domain.repository.GitTrendingRepository


class GitTrendingRepositoryImp constructor(private val remoteDataSource: RemoteDataSource) :
    GitTrendingRepository {
    override suspend fun getRepositories(): Result<List<RepositoryItemEntity>> {
        return remoteDataSource.getRepositories().map {
            it.toRepositoryEntity()
        }
    }
}