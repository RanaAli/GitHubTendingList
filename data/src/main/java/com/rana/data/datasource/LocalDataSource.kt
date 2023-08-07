package com.rana.data.datasource

import com.rana.data.db.TrendingRepoDao
import com.rana.data.models.RepositoryItemDto
import com.rana.domain.entity.RepositoryItemEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val trendingRepoDao: TrendingRepoDao
) {
    suspend fun getRepositories(): Result<List<RepositoryItemEntity>> {
        val repos = trendingRepoDao.allTrendingRepos()
        return if (repos.isNotEmpty())
            Result.success(repos).map { it.repositoryItemEntity() }
        else Result.failure(Exception("No items found"))
    }

    private fun List<RepositoryItemDto>.repositoryItemEntity() =
        map {
            RepositoryItemEntity(
                name = it.name,
                avatar = it.avatar,
                score = it.score,
                url = it.url,
                description = it.description,
                language = it.language

            )
        }

    suspend fun saveRepositories(repos: List<RepositoryItemDto>) {
        trendingRepoDao.saveTrendingRepos(repos)
    }

    suspend fun isReposCacheAvailable() = trendingRepoDao.isReposCacheAvailable()

    suspend fun deleteAllTrendingRepos() = trendingRepoDao.deleteAllTrendingRepos()
}