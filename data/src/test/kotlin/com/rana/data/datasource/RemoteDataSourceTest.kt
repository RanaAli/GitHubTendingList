package com.rana.data.datasource

import com.rana.data.models.response.RepositoryResponse
import com.rana.data.remote.GitHubService
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

class RemoteDataSourceTest {

    @Mock
    private lateinit var mockGitHubService: GitHubService

    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        remoteDataSource = RemoteDataSource(mockGitHubService)
    }

    @Test
    fun `getTrendingRepositories returns data from GitHubService`() = runBlocking {
        // Given
        val query = "kotlin"
        val sort = "stars"
        val order = "desc"
        val mockApiResponse = RepositoryResponse(totalCount = 0, incompleteResults = false, items = emptyList())
        val successResponse: Response<RepositoryResponse> = Response.success(mockApiResponse)

        Mockito.`when`(mockGitHubService.getTrendingRepositories(query, sort, order)).thenReturn(successResponse)

        // When
        val result = remoteDataSource.getTrendingRepositories(query, sort, order)

        // Then
        assert(result.isSuccessful)
        assert(result.body() == mockApiResponse)
        Mockito.verify(mockGitHubService).getTrendingRepositories(query, sort, order)
    }

    // TODO: Add tests for error responses, exceptions, etc.
}