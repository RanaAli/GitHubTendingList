package com.rana.data.repository

import com.rana.data.datasource.LocalDataSource
import com.rana.data.datasource.RemoteDataSource
import com.rana.data.models.RepositoryItemDto
import com.rana.data.utils.SharedPrefsHelper
import com.rana.data.utils.SharedPrefsHelper.Companion.PREF_KEY_REPO_LAST_UPDATED_TIME
import com.rana.data.utils.isTimeWithInInterval
import com.rana.domain.entity.RepositoryItemEntity
import com.rana.domain.repository.GitTrendingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


class GitTrendingRepositoryImp @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val sharedPrefsHelper: SharedPrefsHelper
) : GitTrendingRepository {
    override suspend fun getRepositories(): Flow<Result<List<RepositoryItemEntity>>> {
        val isCacheAvailable = localDataSource.isReposCacheAvailable() > 0

        return if (isCacheAvailable && !ifTimeSurpassed())
            localDataSource.getRepositories()
        else
            getFromRemoteDataSource()
    }

    private fun ifTimeSurpassed(): Boolean {
        val syncUpIntervalInSeconds = 2L * 3_600 // 2 hours
        val lastSyncUpTime = sharedPrefsHelper[PREF_KEY_REPO_LAST_UPDATED_TIME, 0L]
        return isTimeWithInInterval(
            syncUpIntervalInSeconds,
            System.currentTimeMillis(),
            lastSyncUpTime
        )
    }

    private suspend fun getFromRemoteDataSource() =
        remoteDataSource.getRepositories().onEach { repos ->

            if (repos.isSuccess) {
                localDataSource.deleteAllTrendingRepos()
                localDataSource.saveRepositories(repos.getOrThrow().toDTO())
                sharedPrefsHelper.put(
                    PREF_KEY_REPO_LAST_UPDATED_TIME,
                    System.currentTimeMillis()
                )
            }
        }

    private fun List<RepositoryItemEntity>.toDTO(): List<RepositoryItemDto> {
        val finalList = mutableListOf<RepositoryItemDto>()

        forEachIndexed { index, item ->
            finalList.add(
                RepositoryItemDto(
                    uid = index,
                    name = item.name,
                    avatar = item.avatar,
                    score = item.score,
                    url = item.url,
                    description = item.description,
                    language = item.language
                )
            )
        }

        return finalList
    }
}
