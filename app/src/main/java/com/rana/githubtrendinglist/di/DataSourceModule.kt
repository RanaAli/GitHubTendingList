package com.rana.githubtrendinglist.di

import com.rana.data.datasource.LocalDataSource
import com.rana.data.datasource.RemoteDataSource
import com.rana.data.db.TrendingRepoDao
import com.rana.data.remote.GitHubApi
import com.rana.data.remote.GitHubService
import com.rana.data.remote.GithubServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataSourceModuleBind {
    @Singleton
    @Provides
    fun bindRemoteDataSource(service: GitHubService) = RemoteDataSource(service)

    @Singleton
    @Provides
    fun bindGithubService(api: GitHubApi): GitHubService = GithubServiceImpl(api)

    @Singleton
    @Provides
    fun bindLocalDataSource(trendingRepoDao: TrendingRepoDao) = LocalDataSource(trendingRepoDao)
}