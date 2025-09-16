package com.rana.data.datasource

import com.rana.data.db.TrendingRepoDao
import com.rana.data.models.RepositoryItemDto
import com.rana.domain.entity.RepositoryItemEntity // Assuming this path is correct
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow // Added for throwing exception in flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest // Using runTest for coroutine testing
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail // Added for failing test if exception not thrown
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when` // Alias for Mockito.when
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class LocalDataSourceTest {

    @Mock
    private lateinit var mockDao: TrendingRepoDao

    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this) // Initializes mocks
        localDataSource = LocalDataSource(mockDao)
    }

    // --- Tests for getRepositories() ---

    @Test
    fun `getRepositories WHEN dao returns non-empty list THEN returns success with mapped entities`() = runTest {
        // Given
        val mockDtoList = listOf(
            RepositoryItemDto(uid = 1, name = "Repo1", avatar = "avatar1", score = "100", url = "url1", description = "Desc1", language = "Kotlin"),
            RepositoryItemDto(uid = 2, name = "Repo2", avatar = "avatar2", score = "200", url = "url2", description = "Desc2", language = "Java")
        )
        val expectedEntityList = mockDtoList.map {
            RepositoryItemEntity(
                name = it.name,
                avatar = it.avatar,
                score = it.score,
                url = it.url,
                description = it.description,
                language = it.language
            )
        }
        `when`(mockDao.allTrendingRepos()).thenReturn(flowOf(mockDtoList))

        // When
        val result = localDataSource.getRepositories().first()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedEntityList, result.getOrNull())
        verify(mockDao).allTrendingRepos()
    }

    @Test
    fun `getRepositories WHEN dao returns empty list THEN returns failure`() = runTest {
        // Given
        val emptyDtoList = emptyList<RepositoryItemDto>()
        `when`(mockDao.allTrendingRepos()).thenReturn(flowOf(emptyDtoList))

        // When
        val result = localDataSource.getRepositories().first()

        // Then
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
        assertEquals("No items found", result.exceptionOrNull()?.message)
        verify(mockDao).allTrendingRepos()
    }

    @Test
    fun `getRepositories WHEN dao flow throws exception THEN result flow also throws exception`() = runTest {
        // Given
        val expectedException = RuntimeException("DB error")
        `when`(mockDao.allTrendingRepos()).thenReturn(flow { throw expectedException })

        // When & Then
        try {
            localDataSource.getRepositories().first()
            fail("Expected exception was not thrown")
        } catch (e: Exception) {
            assertEquals(expectedException, e)
        }
        verify(mockDao).allTrendingRepos()
    }

    // --- Test for saveRepositories() ---

    @Test
    fun `saveRepositories WHEN called THEN calls dao saveTrendingRepos`() = runTest {
        // Given
        val mockDtoList = listOf(
            RepositoryItemDto(uid = 1, name = "Repo1", avatar = "avatar1", score = "100", url = "url1", description = "Desc1", language = "Kotlin")
        )

        // When
        localDataSource.saveRepositories(mockDtoList)

        // Then
        verify(mockDao).saveTrendingRepos(mockDtoList)
    }

    // --- Tests for isReposCacheAvailable() ---

    @Test
    fun `isReposCacheAvailable WHEN dao returns count greater than 0 THEN returns that count`() = runTest {
        // Given
        val count = 5
        `when`(mockDao.isReposCacheAvailable()).thenReturn(count)

        // When
        val result = localDataSource.isReposCacheAvailable()

        // Then
        assertEquals(count, result)
        verify(mockDao).isReposCacheAvailable()
    }

    @Test
    fun `isReposCacheAvailable WHEN dao returns 0 THEN returns 0`() = runTest {
        // Given
        val count = 0
        `when`(mockDao.isReposCacheAvailable()).thenReturn(count)

        // When
        val result = localDataSource.isReposCacheAvailable()

        // Then
        assertEquals(count, result)
        verify(mockDao).isReposCacheAvailable()
    }

    // --- Test for deleteAllTrendingRepos() ---

    @Test
    fun `deleteAllTrendingRepos WHEN called THEN calls dao deleteAllTrendingRepos`() = runTest {
        // When
        localDataSource.deleteAllTrendingRepos()

        // Then
        verify(mockDao).deleteAllTrendingRepos()
    }
}