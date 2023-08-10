package com.rana.data.datasource

import com.rana.data.db.TrendingRepoDao
import com.rana.data.models.RepositoryItemDto
import com.rana.domain.entity.RepositoryItemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val trendingRepoDao: TrendingRepoDao
) {
    fun getRepositories(): Flow<Result<List<RepositoryItemEntity>>> {
        return trendingRepoDao.allTrendingRepos().map {
            if (it.isNotEmpty())
                Result.success(it).map { item -> item.repositoryItemEntity() }
            else
                Result.failure(Exception("No items found"))
        }
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

    fun saveRepositories(repos: List<RepositoryItemDto>) {
        trendingRepoDao.saveTrendingRepos(repos)
    }

    fun isReposCacheAvailable() = trendingRepoDao.isReposCacheAvailable()

    fun deleteAllTrendingRepos() = trendingRepoDao.deleteAllTrendingRepos()
}