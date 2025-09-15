package com.rana.data.datasource

import com.rana.data.db.TrendingRepoDao
import com.rana.data.models.RepositoryItemDto // Assuming this is your DTO/Entity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class LocalDataSourceTest {

    @Mock
    private lateinit var mockDao: TrendingRepoDao

    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        localDataSource = LocalDataSource(mockDao)
    }

    @Test
    fun `getTrendingRepositories returns data from DAO`() = runBlocking {
        // Given
        val mockRepos = listOf(RepositoryItemDto(id = 1, name = "Test Repo", fullName = "user/Test Repo", description = null, stars = 10, language = "Kotlin", owner = null /* Mock or create OwnerDto if needed */))
        Mockito.`when`(mockDao.getTrendingRepositories()).thenReturn(flowOf(mockRepos))

        // When
        val result = localDataSource.getTrendingRepositories().first()

        // Then
        assert(result == mockRepos)
        Mockito.verify(mockDao).getTrendingRepositories()
    }

    @Test
    fun `saveTrendingRepositories calls DAO insert`() = runBlocking {
        // Given
        val mockRepos = listOf(RepositoryItemDto(id = 1, name = "Test Repo", fullName = "user/Test Repo", description = null, stars = 10, language = "Kotlin", owner = null))
        
        // When
        localDataSource.saveTrendingRepositories(mockRepos)

        // Then
        Mockito.verify(mockDao).insertAll(mockRepos)
    }

    // TODO: Add tests for other methods like clearOldData if any
}