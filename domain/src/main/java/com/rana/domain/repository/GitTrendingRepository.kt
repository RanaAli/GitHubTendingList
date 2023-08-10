package com.rana.domain.repository

import com.rana.domain.entity.RepositoryItemEntity
import kotlinx.coroutines.flow.Flow

interface GitTrendingRepository {
    suspend fun getRepositories(): Flow<Result<List<RepositoryItemEntity>>>
}