package com.rana.githubtrendinglist.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rana.domain.usecases.TrendingRepositoryUseCase
import com.rana.githubtrendinglist.list.state.TrendingAction
import com.rana.githubtrendinglist.list.state.TrendingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val useCase: TrendingRepositoryUseCase
) : ViewModel() {

    var trendingState by mutableStateOf(TrendingState())
        private set

    init {
        reducer(TrendingAction.GetTrendingRepos)
    }

    fun reducer(trendingAction: TrendingAction) {
        when (trendingAction) {
            is TrendingAction.GetTrendingRepos -> getTrendingRepos()
        }
    }

    private fun getTrendingRepos() {
        viewModelScope.launch {

            trendingState = TrendingState(isLoading = true)
            val result = useCase.invoke()

            result.onSuccess {
                trendingState = TrendingState(repos = it)
            }.onFailure {
                trendingState = TrendingState(isError = true)
            }
        }
    }
}