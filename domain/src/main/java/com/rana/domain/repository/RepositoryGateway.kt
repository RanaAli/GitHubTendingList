package com.rana.domain.repository

import com.rana.domain.entity.RepositoryItemEntity

interface RepositoryGateway {
    suspend fun getRepositories(): Result<List<RepositoryItemEntity>>
}