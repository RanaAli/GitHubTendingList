package com.rana.domain.usecases

import com.rana.domain.repository.GitTrendingRepository


class TrendingRepositoryUseCase constructor(
    private val searchRepository: GitTrendingRepository
) {
    suspend operator fun invoke() = searchRepository.getRepositories()
}