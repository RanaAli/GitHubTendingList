package com.rana.data.remote

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.rana.data.models.response.RepositoryResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

class GithubServiceImplTest {

    @MockK
    private lateinit var gitHubApi: GitHubApi

    private lateinit var githubService: GithubServiceImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        githubService = GithubServiceImpl(gitHubApi)
    }

    @Test
    fun `getRepositories calls API with correct parameters`() = runTest {
        // Given
        val query = "language:kotlin"
        val sort = "stars"
        val mockResponse = RepositoryResponse(items = emptyList())
        coEvery { gitHubApi.getRepositories(query, sort) } returns mockResponse

        // When
        val result = githubService.getRepositories(query, sort).first()

        // Then
        assertNotNull(result)
        assertEquals(mockResponse, result)
        coVerify(exactly = 1) { gitHubApi.getRepositories(query, sort) }
    }

    @Test
    fun `getRepositories with different sort parameter calls API correctly`() = runTest {
        // Given
        val query = "language:kotlin"
        val sort = "forks"
        val mockResponse = RepositoryResponse(items = emptyList())
        coEvery { gitHubApi.getRepositories(query, sort) } returns mockResponse

        // When
        val result = githubService.getRepositories(query, sort).first()

        // Then
        assertNotNull(result)
        assertEquals(mockResponse, result)
        coVerify(exactly = 1) { gitHubApi.getRepositories(query, sort) }
    }

    @Test(expected = Exception::class)
    fun `getRepositories propagates API errors`() = runTest {
        // Given
        val query = "language:kotlin"
        val sort = "stars"
        coEvery { gitHubApi.getRepositories(query, sort) } throws Exception("API Error")

        // When/Then
        githubService.getRepositories(query, sort).first() // Should throw exception
    }

    @Test
    fun `getRepositories emits API response in flow`() = runTest {
        // Given
        val query = "language:kotlin"
        val sort = "stars"
        val mockResponse = RepositoryResponse(items = emptyList())
        coEvery { gitHubApi.getRepositories(query, sort) } returns mockResponse

        // When
        val result = githubService.getRepositories(query, sort).first()

        // Then
        assertNotNull(result)
        assertEquals(mockResponse, result)
    }
}
