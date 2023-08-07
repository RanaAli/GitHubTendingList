package com.rana.data


import com.rana.data.datasource.RepoDataSource
import com.rana.data.models.response.RepositoryItemResponse
import com.rana.data.repository.GitTrendingRepositoryImp
import com.rana.domain.entity.RepositoryItemEntity
import com.rana.domain.repository.GitTrendingRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GitTrendingRepositoryTest {

    @MockK
    private lateinit var remoteDataSource: RepoDataSource

    private lateinit var gitTrendingRepositoryGateWay: GitTrendingRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        gitTrendingRepositoryGateWay = GitTrendingRepositoryImp(remoteDataSource)
    }
    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun getRepositories_successApiCall_returnSuccessResultWithRepoList() = runTest {
        //Given list of repos response
        val repos = listOf(RepositoryItemResponse(name = "Test"))
        coEvery { remoteDataSource.getRepositories() } returns Result.success(repos)

        //When get repositories
        val result = gitTrendingRepositoryGateWay.getRepositories()
        val expected = listOf(RepositoryItemEntity(name = "Test"))
        //Then result is success map to domain object successful
        assertThat(result.isSuccess, `is`(true))
        assertThat(result.getOrNull(), `is`(expected))
    }

}