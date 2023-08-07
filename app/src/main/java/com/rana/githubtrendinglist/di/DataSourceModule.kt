package com.rana.githubtrendinglist.di

import com.rana.data.datasource.RemoteDataSource
import com.rana.data.datasource.RepositoriesRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface DataSourceModuleBind {
    @Binds
    abstract fun bindRemoteDataSource(repositoriesRemoteDataSource: RepositoriesRemoteDataSource): RemoteDataSource
}