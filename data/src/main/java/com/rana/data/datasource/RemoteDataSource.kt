package com.rana.data.datasource

import com.rana.data.mapper.toRepositoryItemEntity
import com.rana.data.remote.GitHubApi
import com.rana.domain.entity.RepositoryItemEntity
import java.io.IOException
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val repositoryApiService: GitHubApi
)  {
     suspend fun getRepositories(): Result<List<RepositoryItemEntity>> {
        return try {
            val repositories = repositoryApiService.getRepositories()

            return if (repositories.items != null) {
                Result.success(repositories.items).map { it.toRepositoryItemEntity() }
            } else {
                Result.failure(Exception("No items found"))
            }
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

}
