package com.rana.data.datasource

import com.rana.data.mapper.toRepositoryItemEntity
import com.rana.data.remote.GitHubService
import com.rana.domain.entity.RepositoryItemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val repositoryApiService: GitHubService
) {
    suspend fun getRepositories(): Flow<Result<List<RepositoryItemEntity>>> {
        return repositoryApiService
            .getRepositories()
            .map {
                if (it.items != null) {
                    Result.success(it.items).map { item -> item.toRepositoryItemEntity() }
                } else {
                    Result.failure(Exception("No items found"))
                }
            }
    }
}

