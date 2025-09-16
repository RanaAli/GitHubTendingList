package com.rana.data.repository

import com.rana.data.datasource.LocalDataSource
import com.rana.data.datasource.RemoteDataSource
import com.rana.data.models.RepositoryItemDto
import com.rana.data.utils.SharedPrefsHelper
import com.rana.domain.entity.RepositoryItemEntity
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GitTrendingRepositoryImpTest {

    @MockK
    private lateinit var remoteDataSource: RemoteDataSource

    @MockK
    private lateinit var localDataSource: LocalDataSource

    @MockK
    private lateinit var sharedPrefsHelper: SharedPrefsHelper

    private lateinit var repository: GitTrendingRepositoryImp

    private val currentTime = System.currentTimeMillis()
    private val withinCacheWindow = currentTime - (1L * 3600 * 1000) // 1 hour ago
    private val outsideCacheWindow = currentTime - (3L * 3600 * 1000) // 3 hours ago

    private val sampleEntities = listOf(
        RepositoryItemEntity(
            name = "repo1",
            avatar = "avatar1",
            score = "100",
            url = "url1",
            description = "desc1",
            language = "Kotlin"
        ),
        RepositoryItemEntity(
            name = "repo2",
            avatar = "avatar2",
            score = "200",
            url = "url2",
            description = "desc2",
            language = "Java"
        )
    )

    private val sampleDtos = listOf(
        RepositoryItemDto(
            uid = 0,
            name = "repo1",
            avatar = "avatar1",
            score = "100",
            url = "url1",
            description = "desc1",
            language = "Kotlin"
        ),
        RepositoryItemDto(
            uid = 1,
            name = "repo2",
            avatar = "avatar2",
            score = "200",
            url = "url2",
            description = "desc2",
            language = "Java"
        )
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = GitTrendingRepositoryImp(remoteDataSource, localDataSource, sharedPrefsHelper)
    }

    @Test
    fun `getRepositories returns cached data when available and not expired`() = runTest {
        // Given
        coEvery { localDataSource.isReposCacheAvailable() } returns 1
        coEvery { localDataSource.getRepositories() } returns flowOf(Result.success(sampleEntities))
        every { sharedPrefsHelper[SharedPrefsHelper.PREF_KEY_REPO_LAST_UPDATED_TIME, 0L] } returns currentTime

        // When
        val result = repository.getRepositories().first()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(sampleEntities, result.getOrNull())
        coVerify(exactly = 0) { remoteDataSource.getRepositories() }
        coVerify(exactly = 1) { localDataSource.getRepositories() }
    }

    @Test
    fun `getRepositories fetches from remote when cache is empty`() = runTest {
        // Given
        coEvery { localDataSource.isReposCacheAvailable() } returns 0
        coEvery { remoteDataSource.getRepositories() } returns flowOf(Result.success(sampleEntities))
        coEvery { localDataSource.deleteAllTrendingRepos() } just runs
        coEvery { localDataSource.saveRepositories(any()) } just runs
        every { sharedPrefsHelper[SharedPrefsHelper.PREF_KEY_REPO_LAST_UPDATED_TIME, 0L] } returns 0L
        every { sharedPrefsHelper.put(SharedPrefsHelper.PREF_KEY_REPO_LAST_UPDATED_TIME, any<Long>()) } just runs

        // When
        val result = repository.getRepositories().first()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(sampleEntities, result.getOrNull())
        coVerify(exactly = 1) { remoteDataSource.getRepositories() }
        coVerify(exactly = 1) { localDataSource.deleteAllTrendingRepos() }
        coVerify(exactly = 1) { localDataSource.saveRepositories(sampleDtos) }
    }

//    @Test
//    fun `getRepositories fetches from remote when cache is expired`() = runTest {
//        // Given
//        coEvery { localDataSource.isReposCacheAvailable() } returns 1
//        coEvery { remoteDataSource.getRepositories() } returns flowOf(Result.success(sampleEntities))
//        coEvery { localDataSource.deleteAllTrendingRepos() } just runs
//        coEvery { localDataSource.saveRepositories(any()) } just runs
//        every { sharedPrefsHelper[SharedPrefsHelper.PREF_KEY_REPO_LAST_UPDATED_TIME, 0L] } returns (currentTime - 3600000)
//
//        // When
//        val result = repository.getRepositories().first()
//
//        // Then
//        assertTrue(result.isSuccess)
//        assertEquals(sampleEntities, result.getOrNull())
//        coVerify(exactly = 1) { remoteDataSource.getRepositories() }
//        coVerify(exactly = 1) { localDataSource.deleteAllTrendingRepos() }
//        coVerify(exactly = 1) { localDataSource.saveRepositories(sampleDtos) }
//    }

    @Test
    fun `getRepositories propagates remote errors`() = runTest {
        // Given
        coEvery { localDataSource.isReposCacheAvailable() } returns 0
        coEvery { remoteDataSource.getRepositories() } returns flowOf(
            Result.failure(IOException("Network error"))
        )

        // When
        val result = repository.getRepositories().first()

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)
        coVerify(exactly = 0) { localDataSource.deleteAllTrendingRepos() }
        coVerify(exactly = 0) { localDataSource.saveRepositories(any()) }
    }

    @Test
    fun `getRepositories correctly transforms entities to DTOs`() = runTest {
        // Given
        coEvery { localDataSource.isReposCacheAvailable() } returns 0
        coEvery { remoteDataSource.getRepositories() } returns flowOf(Result.success(sampleEntities))
        coEvery { localDataSource.deleteAllTrendingRepos() } just runs
        coEvery { localDataSource.saveRepositories(any()) } just runs
        val timestamp = slot<Long>()
        every { sharedPrefsHelper.put(any(), capture(timestamp)) } returns Unit

        // When
        repository.getRepositories().first()

        // Then
        coVerify {
            localDataSource.saveRepositories(match { dtos ->
                dtos.size == sampleDtos.size &&
                dtos.zip(sampleDtos).all { (actual, expected) ->
                    actual.uid == expected.uid &&
                    actual.name == expected.name &&
                    actual.avatar == expected.avatar &&
                    actual.score == expected.score &&
                    actual.url == expected.url &&
                    actual.description == expected.description &&
                    actual.language == expected.language
                }
            })
        }
        verify { sharedPrefsHelper.put(SharedPrefsHelper.PREF_KEY_REPO_LAST_UPDATED_TIME, any<Long>()) }
    }

    @Test
    fun `getRepositories updates timestamp when fetching from remote succeeds`() = runTest {
        // Given
        coEvery { localDataSource.isReposCacheAvailable() } returns 0
        coEvery { remoteDataSource.getRepositories() } returns flowOf(Result.success(sampleEntities))
        coEvery { localDataSource.deleteAllTrendingRepos() } just runs
        coEvery { localDataSource.saveRepositories(any()) } just runs
        val timestamp = slot<Long>()
        every { sharedPrefsHelper.put(any(), capture(timestamp)) } returns Unit

        // When
        repository.getRepositories().first()

        // Then
        verify { sharedPrefsHelper.put(SharedPrefsHelper.PREF_KEY_REPO_LAST_UPDATED_TIME, any<Long>()) }
        assertTrue("Timestamp should be around current time",
            System.currentTimeMillis() - timestamp.captured < 1000)
    }
}
