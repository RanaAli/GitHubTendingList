package com.rana.data.datasource

import com.rana.data.models.response.OwnerResponse
import com.rana.data.models.response.RepositoryItemResponse
import com.rana.data.models.response.RepositoryResponse
import com.rana.data.remote.GitHubService
import com.rana.domain.entity.RepositoryItemEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

// New extension function using actual DTOs
fun RepositoryItemResponse.toRepositoryItemEntity(): RepositoryItemEntity {
    return RepositoryItemEntity(
        name = this.name ?: "",
        avatar = this.owner?.avatarUrl ?: "",
        score = this.score ?: "0",
        url = this.url ?: "",
        description = this.description ?: "",
        language = this.language ?: ""
    )
}

@ExperimentalCoroutinesApi
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
    fun `getRepositories WHEN service returns items THEN returns success with mapped entities`() = runTest {
        // Given
        val mockApiItems = listOf(
            RepositoryItemResponse(
                name = "Repo1",
                owner = OwnerResponse(avatarUrl = "avatar1"),
                score = "100",
                url = "url1",
                description = "Desc1",
                language = "Kotlin"
            ),
            RepositoryItemResponse(
                name = "Repo2",
                owner = OwnerResponse(avatarUrl = "avatar2"),
                score = "200",
                url = "url2",
                description = "Desc2",
                language = "Java"
            )
        )
        val mockApiResponse = RepositoryResponse(items = mockApiItems)
        val expectedEntities = mockApiItems.map { it.toRepositoryItemEntity() }

        `when`(mockGitHubService.getRepositories()).thenReturn(flowOf(mockApiResponse))

        // When
        val result = remoteDataSource.getRepositories().first()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedEntities, result.getOrNull())
    }

    @Test
    fun `getRepositories WHEN service returns null items THEN returns failure`() = runTest {
        val mockApiResponse = RepositoryResponse(items = null)
        `when`(mockGitHubService.getRepositories()).thenReturn(flowOf(mockApiResponse))

        val result = remoteDataSource.getRepositories().first()

        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
        assertEquals("No items found", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getRepositories WHEN service returns empty items list THEN returns success with empty entity list`() = runTest {
        val mockApiItems = emptyList<RepositoryItemResponse>()
        val mockApiResponse = RepositoryResponse(items = mockApiItems)
        val expectedEntities = emptyList<RepositoryItemEntity>()

        `when`(mockGitHubService.getRepositories()).thenReturn(flowOf(mockApiResponse))

        val result = remoteDataSource.getRepositories().first()

        assertTrue(result.isSuccess)
        assertEquals(expectedEntities, result.getOrNull())
    }

    @Test
    fun `getRepositories WHEN service flow throws exception THEN result flow also throws exception`() = runTest {
        val expectedException = RuntimeException("Network Error")
        `when`(mockGitHubService.getRepositories()).thenReturn(flow { throw expectedException })

        try {
            remoteDataSource.getRepositories().first()
            assertTrue("Exception was expected but not thrown", false)
        } catch (e: Exception) {
            assertEquals(expectedException, e)
        }
    }

    @Test
    fun `getRepositories WHEN service items have partial null data THEN maps entities with default values`() = runTest {
        // Given
        val mockApiItemsWithNulls = listOf(
            RepositoryItemResponse(name = null, owner = OwnerResponse("avatar1"), score = "100", url = "url1", description = "Desc1", language = "Lang1"),
            RepositoryItemResponse(name = "Repo2", owner = null, score = "200", url = "url2", description = "Desc2", language = "Lang2"),
            RepositoryItemResponse(name = "Repo3", owner = OwnerResponse(null), score = "300", url = "url3", description = "Desc3", language = "Lang3"),
            RepositoryItemResponse(name = "Repo4", owner = OwnerResponse("avatar4"), score = null, url = "url4", description = "Desc4", language = "Lang4"),
            RepositoryItemResponse(name = "Repo5", owner = OwnerResponse("avatar5"), score = "500", url = null, description = "Desc5", language = "Lang5"),
            RepositoryItemResponse(name = "Repo6", owner = OwnerResponse("avatar6"), score = "600", url = "url6", description = null, language = "Lang6"),
            RepositoryItemResponse(name = "Repo7", owner = OwnerResponse("avatar7"), score = "700", url = "url7", description = "Desc7", language = null),
            RepositoryItemResponse(name = "FullRepo", owner = OwnerResponse("avatarFull"), score = "999", url = "urlFull", description = "Full Desc", language = "FullLang")
        )
        val mockApiResponse = RepositoryResponse(items = mockApiItemsWithNulls)

        // The existing extension function will handle the mapping to default values
        val expectedEntities = mockApiItemsWithNulls.map { it.toRepositoryItemEntity() }

        `when`(mockGitHubService.getRepositories()).thenReturn(flowOf(mockApiResponse))

        // When
        val result = remoteDataSource.getRepositories().first()

        // Then
        assertTrue(result.isSuccess)
        val actualEntities = result.getOrNull()
        assertNotNull(actualEntities) // Ensure result is not null before further assertions
        assertEquals(expectedEntities.size, actualEntities!!.size) // Check if sizes match

        // Data classes with proper equals/hashCode implementation allow direct list comparison.
        assertEquals(expectedEntities, actualEntities)
    }
}