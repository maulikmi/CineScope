package com.cinescope.movie.domain.movie.search

import com.cinescope.movie.domain.movie.Movie

data class SearchUiState(
    val query: String = "",
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val totalPages: Int = 0,
    val hasSearched: Boolean = false
)