package com.cinescope.movie.domain.movie.details

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class MovieDetailsStateHolder(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) {

    private val scopeJob = SupervisorJob()
    private val scope = CoroutineScope(scopeJob + Dispatchers.Main)

    private val _uiState = MutableStateFlow(MovieDetailsUiState())
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()


    fun clear() {
        scopeJob.cancel()
    }

    fun loadMovieDetails(movieID: Int) {
        scope.launch {
            _uiState.value = MovieDetailsUiState(isLoading = true)
            try {
                val movie = getMovieDetailsUseCase.invoke(movieID)
                _uiState.value = MovieDetailsUiState(movie = movie, isLoading = false)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = MovieDetailsUiState(
                    isLoading = false,
                    error = e.message ?: "Something went wrong"
                )
            }
        }
    }

}