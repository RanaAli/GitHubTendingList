package com.rana.data.repository

import com.rana.data.models.entity.RepositoryItemEntity

interface RepositoryGateway {
    suspend fun getRepositories(): Result<List<RepositoryItemEntity>>
}