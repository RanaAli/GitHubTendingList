package com.rana.data.repository

import android.util.Log
import com.rana.data.datasource.LocalDataSource
import com.rana.data.datasource.RemoteDataSource
import com.rana.data.utils.SharedPrefsHelper
import com.rana.data.utils.isTimeWithInInterval
import com.rana.domain.entity.RepositoryItemEntity
import com.rana.domain.repository.GitTrendingRepository
import javax.inject.Inject


class GitTrendingRepositoryImp @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val sharedPrefsHelper: SharedPrefsHelper
) :
    GitTrendingRepository {
    override suspend fun getRepositories(): Result<List<RepositoryItemEntity>> {

        val syncUpIntervalInSeconds = 2L * 3_600
        val isCacheAvailable = localDataSource.isReposCacheAvailable() > 0
        val lastSyncUpTime =
            sharedPrefsHelper[SharedPrefsHelper.PREF_KEY_REPO_LAST_UPDATED_TIME, 0L]
        val isTimeSurpassed =
            isTimeWithInInterval(
                syncUpIntervalInSeconds,
                System.currentTimeMillis(),
                lastSyncUpTime
            )

        if (isCacheAvailable && !isTimeSurpassed) {
            Log.d(GitTrendingRepositoryImp::class.java.name, "{Cache Found}")
            return localDataSource.getRepositories()
        } else {

            val repos = remoteDataSource.getRepositories()

            if (repos.isSuccess) {
                localDataSource.deleteAllTrendingRepos()
//                localDataSource.saveRepositories(repos.getOrThrow())
                sharedPrefsHelper.put(
                    SharedPrefsHelper.PREF_KEY_REPO_LAST_UPDATED_TIME,
                    System.currentTimeMillis()
                )
            }

            return repos
        }


    }
}