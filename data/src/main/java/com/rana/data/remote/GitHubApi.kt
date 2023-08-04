package com.rana.data.remote

import com.rana.data.models.response.RepositoryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {
    @GET(REPOSITORIES_URL)
    suspend fun getRepositories(
        @Query("q") query: String = "language",
        @Query("sort") sort: String = "stars"
    ): RepositoryResponse
}