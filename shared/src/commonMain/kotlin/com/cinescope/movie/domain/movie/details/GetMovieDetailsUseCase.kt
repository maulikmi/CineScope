package com.cinescope.movie.domain.movie.details

import com.cinescope.movie.domain.movie.MovieRepository

class GetMovieDetailsUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(id: Int) = repository.getMovieDetails(id)
}