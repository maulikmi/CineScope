package com.cinescope.movie.domain.movie.details


data class MovieDetailsUiState(
    val movie: MovieDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)


