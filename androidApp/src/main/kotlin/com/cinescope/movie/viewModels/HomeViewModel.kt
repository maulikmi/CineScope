package com.cinescope.movie.viewModels

import androidx.lifecycle.ViewModel
import com.cinescope.movie.domain.movie.MovieStateHolder
import org.koin.core.context.GlobalContext

class HomeViewModel(
    private val stateHolder: MovieStateHolder =
        GlobalContext.get().get()
) : ViewModel() {

    val uiState = stateHolder.uiState

    init {
        stateHolder.loadPopularMovies()
    }

    fun refreshMovies() = stateHolder.refreshMovies()

    fun loadNextPage() = stateHolder.loadNextPage()

    fun retryLoadNextPage() = stateHolder.retryLoadNextPage()

    fun retryInitialLoad() = stateHolder.loadPopularMovies()

    override fun onCleared() {
        stateHolder.clear()
        super.onCleared()
    }
}

