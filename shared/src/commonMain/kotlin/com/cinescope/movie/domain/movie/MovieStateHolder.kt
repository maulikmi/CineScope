package com.cinescope.movie.domain.movie

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class MovieUiState(
    val movies: List<Movie> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val paginationError: String? = null,
    val currentPage: Int = 0,
    val totalPages: Int = 0
) {
    val hasMore: Boolean
        get() = currentPage < totalPages
}

class MovieStateHolder(
    private val getPopularMovies: GetPopularMoviesUseCase
) {
    private val scope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val _uiState =
        MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> =
        _uiState.asStateFlow()

    fun loadPage(
        page: Int,
        isInitialLoad: Boolean,
        isRefreshing: Boolean = false
    ) {
        scope.launch {
            when {
                isInitialLoad -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = true,
                        error = null
                    )
                }
                isRefreshing -> {
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = true,
                        error = null
                    )
                }

                else -> {
                    _uiState.value = _uiState.value.copy(
                        isLoadingMore = true,
                        error = null
                    )
                }
            }

            try {
                val result = getPopularMovies(page)
                val currentMovies = _uiState.value.movies
                val movies =
                    if (page == 1) {
                        result.movies
                    } else {
                        currentMovies + result.movies
                    }
                _uiState.value = _uiState.value.copy(
                    movies = movies,
                    isRefreshing= false,
                    isLoading = false,
                    isLoadingMore = false,
                    error = null,
                    currentPage = result.page,
                    totalPages = result.totalPages
                )
            } catch (e: Exception) {
                if (isInitialLoad || isRefreshing) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = e.message ?: "Something went wrong"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoadingMore = false,
                        paginationError = e.message
                            ?: "Failed to load more movies"
                    )
                }
            }
        }
    }

    fun retryLoadNextPage() {
        val state = _uiState.value

        if (state.isLoading) return
        if (state.isRefreshing) return
        if (state.isLoadingMore) return
        if (!state.hasMore && state.currentPage != 0) {
            return
        }

        loadPage(
            page = state.currentPage + 1,
            isInitialLoad = false
        )
    }

    fun loadPopularMovies() {
        loadPage(1, isInitialLoad = true)
    }
    fun refreshMovies() {
        loadPage(
            page = 1,
            isInitialLoad = false,
            isRefreshing = true
        )
    }

    private fun canLoadNextPage(state: MovieUiState): Boolean {
        if (state.isLoading) return false
        if (state.isRefreshing) return false
        if (state.isLoadingMore) return false
        if (state.paginationError != null) return false
        if (!state.hasMore && state.currentPage != 0) return false

        return true
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (!canLoadNextPage(state)) {
            return
        }
        loadPage(
            page = state.currentPage + 1,
            isInitialLoad = false
        )
    }
}