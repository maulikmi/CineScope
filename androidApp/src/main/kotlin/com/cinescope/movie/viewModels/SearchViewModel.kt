package com.cinescope.movie.viewModels

import androidx.lifecycle.ViewModel
import com.cinescope.movie.domain.movie.search.SearchStateHolder
import org.koin.core.context.GlobalContext

class SearchViewModel : ViewModel() {

    private val stateHolder: SearchStateHolder =
        GlobalContext.get().get()

    val uiState = stateHolder.uiState

    fun search(query: String) {
        stateHolder.search(query)
    }

    fun loadNextPage() {
        stateHolder.loadNextPage()
    }

    fun retry() {
        stateHolder.retry()
    }

    override fun onCleared() {
        stateHolder.clear()
        super.onCleared()
    }
}