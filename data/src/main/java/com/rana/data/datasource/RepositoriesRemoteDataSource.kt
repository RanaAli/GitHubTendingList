package com.rana.data.datasource

import com.rana.data.models.response.RepositoryItemResponse
import com.rana.data.remote.GitHubApi
import java.io.IOException

class RepositoriesRemoteDataSource constructor(
    private val repositoryApiService: GitHubApi
) : RemoteDataSource {
    override suspend fun getRepositories(): Result<List<RepositoryItemResponse>> {
        return try {
            val repositories = repositoryApiService.getRepositories()

            return if (repositories.items != null) {
                Result.success(repositories.items)
            } else {
                Result.failure(Exception("No items found"))
            }
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

}
