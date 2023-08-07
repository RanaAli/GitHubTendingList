package com.rana.githubtrendinglist.list.state

import com.rana.domain.entity.RepositoryItemEntity

data class TrendingState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val repos: List<RepositoryItemEntity> = emptyList()
)
