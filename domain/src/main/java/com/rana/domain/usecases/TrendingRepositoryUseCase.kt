package com.rana.domain.usecases

import com.rana.domain.repository.GitTrendingRepository
import javax.inject.Inject


class TrendingRepositoryUseCase @Inject constructor(
    private val searchRepository: GitTrendingRepository
): UseCase() {
    override suspend operator fun invoke() = searchRepository.getRepositories()
}