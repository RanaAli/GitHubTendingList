package com.rana.data.datasource

import com.rana.data.models.response.RepositoryItemResponse


interface RemoteDataSource {
    suspend fun getRepositories():Result<List<RepositoryItemResponse>>
}