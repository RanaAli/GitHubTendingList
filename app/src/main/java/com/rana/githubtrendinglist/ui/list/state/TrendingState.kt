package com.rana.githubtrendinglist.ui.list.state

import com.rana.domain.entity.RepositoryItemEntity

sealed class TrendingState {
    data object Loading : TrendingState()
    data class Error(val error: String) : TrendingState()
    data class Success(val repo: List<RepositoryItemEntity>) :
        TrendingState()

    data object Empty : TrendingState()

}