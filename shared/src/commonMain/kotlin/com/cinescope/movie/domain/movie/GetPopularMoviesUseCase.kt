package com.cinescope.movie.domain.movie


class GetPopularMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(
        page: Int = 1
    ): MoviePage {
        return repository.getPopularMovies(page)
    }
}