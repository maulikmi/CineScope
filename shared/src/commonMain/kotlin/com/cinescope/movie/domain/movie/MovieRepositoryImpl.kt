package com.cinescope.movie.domain.movie

import com.cinescope.movie.network.TmdbApi

class MovieRepositoryImpl(
    private val api: TmdbApi
) : MovieRepository {
    override suspend fun getPopularMovies(page: Int): MoviePage {

        println(">> MovieRepository: BEFORE API CALL")

        try {
            val response = api.getPopularMovies(page)

            println(">> MovieRepository: AFTER API CALL")
            println(">> MovieRepository: results = ${response.results.size}")

            return MoviePage(
                page = response.page,
                totalPages = response.totalPages,
                totalResults = response.totalResults,
                movies = response.results.map { dto ->
                    Movie(
                        id = dto.id,
                        title = dto.title,
                        overview = dto.overview,
                        posterPath = dto.posterPath,
                        backdropPath = dto.backdropPath,
                        releaseDate = dto.releaseDate,
                        rating = dto.voteAverage
                    )
                }
            )
        } catch (e: Exception) {
            println(">> MovieRepository: ERROR")
            println(">> MovieRepository: ${e::class.simpleName}")
            println(">> MovieRepository: ${e.message}")

            throw e
        }
    }
}