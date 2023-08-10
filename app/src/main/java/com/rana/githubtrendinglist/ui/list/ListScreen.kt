package com.rana.githubtrendinglist.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rana.domain.entity.RepositoryItemEntity
import com.rana.githubtendinglist.R
import com.rana.githubtrendinglist.ui.list.component.DarkModeMenu
import com.rana.githubtrendinglist.ui.list.component.RetryScreen
import com.rana.githubtrendinglist.ui.list.component.ShimmerList
import com.rana.githubtrendinglist.ui.list.component.TrendingList
import com.rana.githubtrendinglist.ui.list.state.TrendingAction
import com.rana.githubtrendinglist.ui.list.state.TrendingState
import com.rana.githubtrendinglist.ui.list.state.TrendingState.Empty
import com.rana.githubtrendinglist.ui.list.state.TrendingState.Error
import com.rana.githubtrendinglist.ui.list.state.TrendingState.Loading
import com.rana.githubtrendinglist.ui.list.state.TrendingState.Success

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingScreen(
    trendingState: TrendingState,
    isDarkMode: Boolean,
    event: (TrendingAction) -> Unit,
    toggleTheme: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.trending)) },
                actions = { DarkModeMenu(isDarkMode = isDarkMode, toggleDarkMode = toggleTheme) }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.padding(padding),
        ) {
            when (trendingState) {
                is Empty -> Unit
                is Error -> RetryScreen { event(TrendingAction.GetTrendingRepos) }
                Loading -> ShimmerList()
                is Success -> TrendingList(trendingRepos = trendingState.repo)
            }
        }
    }
}

@Composable
@Preview
fun Preview() {
    TrendingScreen(
        trendingState = Success(getFakeRepos()),
        isDarkMode = false,
        event = {},
        toggleTheme = {}
    )
}

@Composable
private fun getFakeRepos() = listOf(
    RepositoryItemEntity(
        name = "Name 1",
        description = "Description....",
        avatar = "https://avatars.githubusercontent.com/u/3034245?v=4"
    ),
    RepositoryItemEntity(
        name = "Name 2",
        description = "Description....",
        avatar = "https://avatars.githubusercontent.com/u/3034245?v=4"
    ),
    RepositoryItemEntity(
        name = "Name 3",
        description = "Description....",
        avatar = "https://avatars.githubusercontent.com/u/3034245?v=4"
    )
)