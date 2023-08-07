package com.example.trending

import com.example.CoroutineTestRule
import com.rana.domain.entity.RepositoryItemEntity
import com.rana.domain.repository.GitTrendingRepository
import com.rana.githubtrendinglist.list.ListViewModel
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.delay
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class TrendingViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @MockK
    private lateinit var repositoryGateway: GitTrendingRepository

    private lateinit var trendingViewModel: ListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun getTrendingRepositories_gatewayReturnsSuccessfulList_updateStateToLoadingThenEmitListOfTrendingRepositories() {

        //Given list of trending repositories
        val repos = listOf(RepositoryItemEntity())
        coEvery { repositoryGateway.getRepositories() } coAnswers {
            delay(1000)
            Result.success(repos)
        }
        //When init viewModel
        trendingViewModel = ListViewModel(repositoryGateway)

        //Then update state with loading then expected list
        assertThat(trendingViewModel.trendingState.isLoading, `is`(true))
        coroutineTestRule.testDispatcher.scheduler.advanceUntilIdle()

        assertThat(trendingViewModel.trendingState.isLoading, `is`(false))
        assertThat(trendingViewModel.trendingState.repos, `is`(repos))
    }

    @Test
    fun getTrendingRepositories_gatewayReturnsFailure_updateStateToLoadingThenEmitError() {

        //Given failure
        coEvery { repositoryGateway.getRepositories() } coAnswers {
            delay(1000)
            Result.failure(IOException("Network error"))
        }
        //When init viewModel
        trendingViewModel = ListViewModel(repositoryGateway)

        //Then update state with loading then expected list
        assertThat(trendingViewModel.trendingState.isLoading, `is`(true))
        coroutineTestRule.testDispatcher.scheduler.advanceUntilIdle()

        assertThat(trendingViewModel.trendingState.isLoading, `is`(false))
        assertThat(trendingViewModel.trendingState.isError, `is`(true))
    }
}