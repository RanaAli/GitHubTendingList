package com.rana.githubtrendinglist.list.state

sealed interface TrendingAction {
    object GetTrendingRepos : TrendingAction
}