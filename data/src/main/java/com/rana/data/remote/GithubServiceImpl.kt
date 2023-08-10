package com.rana.data.remote

import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GithubServiceImpl @Inject constructor(private val service: GitHubApi) : GitHubService {
    override fun getRepositories(query: String, sort: String) = flow {
        emit(service.getRepositories(query, sort))
    }
}