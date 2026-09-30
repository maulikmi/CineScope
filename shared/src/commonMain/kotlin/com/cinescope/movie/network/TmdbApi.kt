package com.cinescope.movie.network

import com.cinescope.movie.domain.movie.MovieResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class TmdbApi(
    private val client: HttpClient
) {
    suspend fun getPopularMovies(
        page: Int = 1
    ): MovieResponseDto {

        println(">> TmdbApi: calling movie/popular")

        return client
            .get("movie/popular") {
                url {
                    parameters.append("page", page.toString())
                }
            }
            .body()
    }
}