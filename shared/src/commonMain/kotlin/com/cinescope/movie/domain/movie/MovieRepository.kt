package com.cinescope.movie.domain.movie

interface MovieRepository {

    suspend fun getPopularMovies(
        page : Int = 1
    ) : MoviePage
}