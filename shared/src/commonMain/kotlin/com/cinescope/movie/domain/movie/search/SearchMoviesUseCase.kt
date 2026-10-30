package com.cinescope.movie.domain.movie.search

import com.cinescope.movie.domain.movie.MoviePage
import com.cinescope.movie.domain.movie.MovieRepository

class SearchMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(
        query: String,
        page: Int = 1
    ): MoviePage {
        require(query.isNotBlank()) {
            "Search query cannot be empty"
        }

        return movieRepository.searchMovies(
            query = query.trim(),
            page = page
        )
    }
}