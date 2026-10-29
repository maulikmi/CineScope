package com.cinescope.movie.viewModels

import androidx.lifecycle.ViewModel
import com.cinescope.movie.domain.movie.details.MovieDetailsStateHolder
import org.koin.core.context.GlobalContext

class MovieDetailsViewModel(
    private val stateHolder: MovieDetailsStateHolder =
        GlobalContext.get().get()
) : ViewModel() {

    val uiState = stateHolder.uiState

    private var requestedMovieId: Int? = null

    fun loadMovieDetails(movieId: Int) {
        // Avoid restarting the same request after configuration changes.
        if (requestedMovieId == movieId) return

        requestedMovieId = movieId
        stateHolder.loadMovieDetails(movieId)
    }

    fun retryMovieDetails(movieId: Int) {
        requestedMovieId = movieId
        stateHolder.loadMovieDetails(movieId)
    }

    override fun onCleared() {
        stateHolder.clear()
        super.onCleared()
    }
}