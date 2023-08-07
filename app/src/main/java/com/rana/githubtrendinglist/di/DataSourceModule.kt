package com.rana.githubtrendinglist.di

import com.rana.data.datasource.LocalDataSource
import com.rana.data.datasource.RemoteDataSource
import com.rana.data.db.TrendingRepoDao
import com.rana.data.remote.GitHubApi
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
    fun bindRemoteDataSource(service: GitHubApi) = RemoteDataSource(service)

    @Singleton
    @Provides
    fun bindLocalDataSource(trendingRepoDao: TrendingRepoDao) = LocalDataSource(trendingRepoDao)
}