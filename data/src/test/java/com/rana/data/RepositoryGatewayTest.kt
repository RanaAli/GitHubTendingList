package com.rana.data


import com.rana.data.datasource.RemoteDataSource
import com.rana.data.models.entity.RepositoryItemEntity
import com.rana.data.models.response.RepositoryItemResponse
import com.rana.data.repository.RepositoryGateway
import com.rana.data.repository.RepositoryGatewayImp
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
class RepositoryGatewayTest {

    @MockK
    private lateinit var remoteDataSource: RemoteDataSource

    private lateinit var repositoryGateWay: RepositoryGateway

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repositoryGateWay = RepositoryGatewayImp(remoteDataSource)
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
        val result = repositoryGateWay.getRepositories()
        val expected = listOf(RepositoryItemEntity(name = "Test"))
        //Then result is success map to domain object successful
        assertThat(result.isSuccess, `is`(true))
        assertThat(result.getOrNull(), `is`(expected))
    }

}