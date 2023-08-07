package com.rana.githubtrendinglist.di

import com.rana.domain.usecases.TrendingRepositoryUseCase
import com.rana.domain.usecases.UseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface UseCaseModule {
    @Binds
    fun bindUseCase(useCase: TrendingRepositoryUseCase): UseCase
}