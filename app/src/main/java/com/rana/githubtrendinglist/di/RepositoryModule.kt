package com.rana.githubtrendinglist.di

import com.rana.data.repository.GitTrendingRepositoryImp
import com.rana.domain.repository.GitTrendingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {
    @Binds
    fun bindRepoGateway(repositoryGatewayImp: GitTrendingRepositoryImp): GitTrendingRepository
}