package com.rana.data


import com.google.gson.Gson
import com.rana.data.datasource.RemoteDataSource
import com.rana.data.datasource.RepositoriesRemoteDataSource
import com.rana.data.models.response.RepositoryResponse
import com.rana.data.remote.GitHubApi
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class RemoteDataSourceTest {

    @MockK
    private lateinit var apiService: GitHubApi

    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        remoteDataSource = RepositoriesRemoteDataSource(apiService)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun getRemoteRepositories_apiCallSuccessful_successfulResultWithRepositoriesList() = runTest {
        val response = Gson().fromJson(RemoteDataSourceTest_Stub, RepositoryResponse::class.java)

        coEvery { apiService.getRepositories() } returns response

        val result = remoteDataSource.getRepositories()

        assertThat(result.isSuccess, `is`(true))
        assertThat(result.getOrNull(), `is`(response.items))
        assertThat(result.getOrNull()?.get(0)?.name, `is`("go"))
        assertThat(result.getOrNull()?.get(1)?.name, `is`("ant-design"))
        assertThat(result.getOrNull()?.get(2)?.name, `is`("swift"))
    }

    @Test
    fun getRemoteRepositories_apiReturnsNull_returnsFailure() = runTest {
        val response = RepositoryResponse(items = null)
        coEvery { apiService.getRepositories() } returns response

        val result = remoteDataSource.getRepositories()

        assertThat(result.isFailure, `is`(true))
    }

    @Test
    fun getRemoteRepositories_throwsIOException_returnsFailure() = runTest {
        coEvery { apiService.getRepositories() } throws IOException("Network error")

        val result = remoteDataSource.getRepositories()

        assertThat(result.isFailure, `is`(true))
    }
}