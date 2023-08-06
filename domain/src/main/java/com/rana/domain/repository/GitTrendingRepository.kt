package com.rana.domain.repository

import com.rana.domain.entity.RepositoryItemEntity

interface GitTrendingRepository {
    suspend fun getRepositories(): Result<List<RepositoryItemEntity>>
}