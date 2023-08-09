package com.rana.githubtrendinglist

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.rana.domain.entity.RepositoryItemEntity
import com.rana.githubtrendinglist.ui.list.TrendingScreen
import com.rana.githubtrendinglist.ui.list.state.TrendingState
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class TrendingScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun testTrendingScreen_stateContainsList_listFoundWithTwoItems() {
        //Given success state with repos list
        val state = TrendingState(
            repos = listOf(
                RepositoryItemEntity(
                    name = "Repo 1",
                    description = "Description 1",
                ),
                RepositoryItemEntity(
                    name = "Repo 2",
                    description = "Description 2",
                )
            ),
        )
        //When show screen
        composeTestRule.setContent {
            TrendingScreen(
                trendingState = state,
                isDarkMode = isSystemInDarkTheme(),
                event = {},
                {}
            )
        }

        // Check if top app bar title is displayed correctly
        composeTestRule.onNodeWithText("Trending").assertExists()
        // Then trending list should show with repos items
        composeTestRule.onNodeWithTag("TrendingList").assertExists()
        composeTestRule.onAllNodesWithTag("TrendingListItem").assertCountEquals(2)
    }

    @Test
    fun testTrendingScreen_stateIsLoading_shouldShowShimmer() {
        //Given loading state
        val state = TrendingState(isLoading = true)
        //When show screen
        composeTestRule.setContent {
            TrendingScreen(
                trendingState = state,
                isDarkMode = isSystemInDarkTheme(),
                event = {},
                {}
            )
        }

        // Check if top app bar title is displayed correctly
        composeTestRule.onNodeWithText("Trending").assertExists()
        // Then trending list should show shimmer
        composeTestRule.onNodeWithTag("shimmerList").assertExists()
    }

    // Should stop the animation before run because it contains lottie animation.
    @Test
    fun testTrendingScreen_stateIsError_shouldShowErrorScreen() {
        //Given error state
        var state = TrendingState(isError = true)
        composeTestRule.setContent {
            TrendingScreen(
                trendingState = state,
                isDarkMode = isSystemInDarkTheme(),
                event = { state = TrendingState(isLoading = true) }
            ) { }
        }

        // Check if top app bar title is displayed correctly
        composeTestRule.onNodeWithText("Trending").assertExists()
        // Then should show retry screen
        composeTestRule.onNodeWithTag("errorAnimation").assertExists()
        composeTestRule.onNodeWithTag("Retry button").assertExists()
        composeTestRule.onNodeWithTag("Retry button").performClick()

        assertThat(state.isLoading, `is`(true))

    }
}