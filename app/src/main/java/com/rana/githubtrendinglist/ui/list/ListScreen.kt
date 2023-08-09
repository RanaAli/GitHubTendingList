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
import com.rana.githubtendinglist.R
import com.rana.githubtrendinglist.ui.list.component.DarkModeMenu
import com.rana.githubtrendinglist.ui.list.component.RetryScreen
import com.rana.githubtrendinglist.ui.list.component.ShimmerList
import com.rana.githubtrendinglist.ui.list.component.TrendingList
import com.rana.githubtrendinglist.ui.list.state.TrendingAction
import com.rana.githubtrendinglist.ui.list.state.TrendingState

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
            when {
                trendingState.isLoading -> ShimmerList()
                trendingState.isError -> RetryScreen { event(TrendingAction.GetTrendingRepos) }
                else -> TrendingList(trendingRepos = trendingState.repos)
            }
        }
    }
}