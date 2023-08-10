package com.rana.data.remote

import com.rana.data.models.response.RepositoryResponse
import kotlinx.coroutines.flow.Flow

interface GitHubService {
    fun getRepositories(
        query: String = "language",
        sort: String = "stars"
    ): Flow<RepositoryResponse>

}