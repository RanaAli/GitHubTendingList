package com.rana.data.db

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rana.data.models.RepositoryItemDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(
    sdk = [33], // Updated to Android 13
    manifest = Config.NONE,
    application = Application::class
)
class TrendingRepoDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var trendingRepoDao: TrendingRepoDao

    // Sample DTOs for testing
    private val repo1 = RepositoryItemDto(uid = 1, name = "Repo1", avatar = "avatar1", score = "100", url = "url1", description = "Desc1", language = "Kotlin")
    private val repo2 = RepositoryItemDto(uid = 2, name = "Repo2", avatar = "avatar2", score = "200", url = "url2", description = "Desc2", language = "Java")
    private val repo1Updated = RepositoryItemDto(uid = 1, name = "Repo1 Updated", avatar = "avatar1_updated", score = "150", url = "url1_updated", description = "Desc1 Updated", language = "Kotlin")

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // For simplicity in tests, not recommended for production
            .build()
        trendingRepoDao = appDatabase.trendingRepoDao()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun `isReposCacheAvailable initially returns 0`() = runTest {
        val count = trendingRepoDao.isReposCacheAvailable()
        assertEquals(0, count)
    }

    @Test
    fun `saveTrendingRepos saves data and isReposCacheAvailable returns correct count`() = runTest {
        val reposToSave = listOf(repo1, repo2)
        trendingRepoDao.saveTrendingRepos(reposToSave)

        val count = trendingRepoDao.isReposCacheAvailable()
        assertEquals(2, count)
    }

    @Test
    fun `saveTrendingRepos with empty list does not change cache or count when db is empty`() = runTest {
        // Initial state: DB is empty
        assertEquals(0, trendingRepoDao.isReposCacheAvailable())
        assertTrue(trendingRepoDao.allTrendingRepos().first().isEmpty())

        // Action: Save an empty list
        trendingRepoDao.saveTrendingRepos(emptyList())

        // Assertions: DB should still be empty
        assertEquals(0, trendingRepoDao.isReposCacheAvailable())
        assertTrue(trendingRepoDao.allTrendingRepos().first().isEmpty())
    }

    @Test
    fun `saveTrendingRepos with empty list does not change cache or count when db has data`() = runTest {
        // Initial state: DB has data
        val initialRepos = listOf(repo1, repo2)
        trendingRepoDao.saveTrendingRepos(initialRepos)
        assertEquals(2, trendingRepoDao.isReposCacheAvailable())
        assertEquals(initialRepos, trendingRepoDao.allTrendingRepos().first())

        // Action: Save an empty list
        trendingRepoDao.saveTrendingRepos(emptyList())

        // Assertions: DB should still have the original data, and count should be the same.
        // The OnConflictStrategy.REPLACE for an empty list should not delete existing items.
        assertEquals(2, trendingRepoDao.isReposCacheAvailable())
        assertEquals(initialRepos, trendingRepoDao.allTrendingRepos().first())
    }


    @Test
    fun `allTrendingRepos initially returns empty list`() = runTest {
        val repos = trendingRepoDao.allTrendingRepos().first()
        assertTrue(repos.isEmpty())
    }

    @Test
    fun `allTrendingRepos returns saved repositories`() = runTest {
        val reposToSave = listOf(repo1, repo2)
        trendingRepoDao.saveTrendingRepos(reposToSave)

        val repos = trendingRepoDao.allTrendingRepos().first()
        assertEquals(2, repos.size)
        assertTrue(repos.contains(repo1))
        assertTrue(repos.contains(repo2))
    }

    @Test
    fun `saveTrendingRepos with conflict replace updates existing item`() = runTest {
        trendingRepoDao.saveTrendingRepos(listOf(repo1))
        var repos = trendingRepoDao.allTrendingRepos().first()
        assertEquals("Repo1", repos.find { it.uid == 1 }?.name)

        trendingRepoDao.saveTrendingRepos(listOf(repo1Updated, repo2))

        repos = trendingRepoDao.allTrendingRepos().first()
        assertEquals(2, repos.size)
        assertEquals("Repo1 Updated", repos.find { it.uid == 1 }?.name)
        assertTrue(repos.contains(repo2))
    }


    @Test
    fun `deleteAllTrendingRepos removes all repositories`() = runTest {
        trendingRepoDao.saveTrendingRepos(listOf(repo1, repo2))
        var count = trendingRepoDao.isReposCacheAvailable()
        assertEquals(2, count)

        trendingRepoDao.deleteAllTrendingRepos()

        count = trendingRepoDao.isReposCacheAvailable()
        assertEquals(0, count)

        val repos = trendingRepoDao.allTrendingRepos().first()
        assertTrue(repos.isEmpty())
    }

    @Test
    fun `allTrendingRepos flow emits updates on data change`() = runTest {
        val flow = trendingRepoDao.allTrendingRepos()

        assertEquals(0, flow.first().size)

        trendingRepoDao.saveTrendingRepos(listOf(repo1))
        assertEquals(1, flow.first().size)
        assertEquals(repo1, flow.first()[0])

        trendingRepoDao.saveTrendingRepos(listOf(repo1Updated, repo2))
        val currentList = flow.first()
        assertEquals(2, currentList.size)
        assertEquals("Repo1 Updated", currentList.find { it.uid == 1 }?.name)

        trendingRepoDao.deleteAllTrendingRepos()
        assertEquals(0, flow.first().size)
    }

    @Test
    fun `saveTrendingRepos allows empty fields and retrieves them correctly`() = runTest {
        // Use empty strings for non-nullable fields
        val repoWithEmpties = RepositoryItemDto(
            uid = 3,
            name = "RepoEmpties",
            avatar = "",
            score = "",
            url = "",
            description = "",
            language = ""
        )
        trendingRepoDao.saveTrendingRepos(listOf(repoWithEmpties))
        val repos = trendingRepoDao.allTrendingRepos().first()
        val retrieved = repos.find { it.uid == 3 }
        assertEquals("RepoEmpties", retrieved?.name)
        assertEquals("", retrieved?.avatar)
        assertEquals("", retrieved?.score)
        assertEquals("", retrieved?.url)
        assertEquals("", retrieved?.description)
        assertEquals("", retrieved?.language)
    }

    @Test
    fun `saveTrendingRepos handles large data sets`() = runTest {
        val largeList = (1..1000).map {
            RepositoryItemDto(
                uid = it,
                name = "Repo$it",
                avatar = "avatar$it",
                score = it.toString(),
                url = "url$it",
                description = "Desc$it",
                language = if (it % 2 == 0) "Kotlin" else "Java"
            )
        }
        trendingRepoDao.saveTrendingRepos(largeList)
        val count = trendingRepoDao.isReposCacheAvailable()
        assertEquals(1000, count)
        val repos = trendingRepoDao.allTrendingRepos().first()
        assertEquals(1000, repos.size)
        assertTrue(repos.any { it.name == "Repo500" })
    }
}