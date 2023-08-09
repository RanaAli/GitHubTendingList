package com.rana.githubtrendinglist.ui.list.state

sealed interface TrendingAction {
    object GetTrendingRepos : TrendingAction
}