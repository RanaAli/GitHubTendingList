package com.rana.data.datasource

import com.rana.domain.entity.RepositoryItemEntity


interface RepoDataSource {
    suspend fun getRepositories(): Result<List<RepositoryItemEntity>>
}