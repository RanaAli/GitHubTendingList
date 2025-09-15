package com.rana.data.remote

import com.rana.data.models.response.RepositoryResponse
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

class GithubServiceImplTest {

    @Mock
    private lateinit var mockApi: GitHubApi

    private lateinit var service: GithubServiceImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        service = GithubServiceImpl(mockApi)
    }

    @Test
    fun `getTrendingRepositories returns success`() = runBlocking {
        // Given
        val query = "kotlin"
        val sort = "stars"
        val order = "desc"
        val mockRepoResponse = RepositoryResponse(totalCount = 0, incompleteResults = false, items = emptyList())
        val successResponse: Response<RepositoryResponse> = Response.success(mockRepoResponse)

        Mockito.`when`(mockApi.getTrendingRepositories(query, sort, order)).thenReturn(successResponse)

        // When
        val result = service.getTrendingRepositories(query, sort, order)

        // Then
        assert(result.isSuccessful)
        assert(result.body() == mockRepoResponse)
        Mockito.verify(mockApi).getTrendingRepositories(query, sort, order)
    }

    // TODO: Add tests for error cases, exceptions, etc.
}