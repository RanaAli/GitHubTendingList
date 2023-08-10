package com.rana.githubtrendinglist.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rana.domain.usecases.TrendingRepositoryUseCase
import com.rana.githubtrendinglist.ui.list.state.TrendingAction
import com.rana.githubtrendinglist.ui.list.state.TrendingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val useCase: TrendingRepositoryUseCase
) : ViewModel() {

    private var _trendingState = MutableStateFlow<TrendingState>(TrendingState.Empty)
    val trendingState = _trendingState.asStateFlow()

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

            _trendingState.value = TrendingState.Loading

            withContext(IO) { useCase.invoke() }.onSuccess {
                _trendingState.value = TrendingState.Success(it)
            }.onFailure {
                _trendingState.value = TrendingState.Error(it.message.orEmpty())
            }
        }
    }
}