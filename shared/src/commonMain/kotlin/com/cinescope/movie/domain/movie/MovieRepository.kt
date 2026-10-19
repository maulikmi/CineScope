package com.cinescope.movie.domain.movie

import com.cinescope.movie.domain.movie.details.MovieDetails

interface MovieRepository {

    suspend fun getPopularMovies(
        page : Int = 1
    ) : MoviePage

    suspend fun getMovieDetails(
        movieId: Int
    ): MovieDetails
}