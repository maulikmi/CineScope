package com.cinescope.movie.domain.movie.details

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MovieDetailsDto(
    val id: Int,
    val title: String,
    val overview: String?,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("release_date")
    val releaseDate: String?,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    val runtime: Int?,
    val genres: List<GenreDto>
)

@Serializable
data class GenreDto(
    val id: Int,
    val name: String
)


data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String?,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val rating: Double,
    val voteCount: Int,
    val runtime: Int?,
    val genres: List<Genre>
)

data class Genre(
    val id: Int,
    val name: String
)