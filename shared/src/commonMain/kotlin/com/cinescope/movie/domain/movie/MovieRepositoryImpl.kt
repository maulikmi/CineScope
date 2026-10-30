package com.cinescope.movie.domain.movie

import com.cinescope.movie.domain.movie.details.Genre
import com.cinescope.movie.domain.movie.details.MovieDetails
import com.cinescope.movie.domain.movie.details.ProductionCompany
import com.cinescope.movie.domain.movie.details.ProductionCountry
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

    override suspend fun getMovieDetails(
        movieId: Int
    ): MovieDetails {

        println(">> MovieRepository: getting details for $movieId")

        val response = api.getMovieDetails(movieId)

        return MovieDetails(
            id = response.id,
            title = response.title,
            overview = response.overview,
            posterPath = response.posterPath,
            backdropPath = response.backdropPath,
            releaseDate = response.releaseDate,
            rating = response.voteAverage,
            voteCount = response.voteCount,
            runtime = response.runtime,
            genres = response.genres.map { genre ->
                Genre(
                    id = genre.id,
                    name = genre.name
                )
            },
            tagline = response.tagline,
            status = response.status,
            budget = response.budget,
            revenue = response.revenue,
            originalLanguage = response.originalLanguage,
            homepage = response.homepage,
            imdbId = response.imdbId,
            productionCompanies = response.productionCompanies.map { company ->
                ProductionCompany(
                    id = company.id,
                    name = company.name,
                    logoPath = company.logoPath,
                    originCountry = company.originCountry
                )
            },
            productionCountries = response.productionCountries.map { country ->
                ProductionCountry(
                    iso31661 = country.iso31661,
                    name = country.name
                )
            }
        )
    }

    override suspend fun searchMovies(
        query: String,
        page: Int
    ): MoviePage {
        try {
            val response = api.searchMovies(query,page)

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
