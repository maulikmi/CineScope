package com.cinescope.movie.domain.movie.details

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDto(
    val id: Int,
    val title: String,
    val overview: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("vote_count")
    val voteCount: Int = 0,
    val runtime: Int? = null,
    val genres: List<GenreDto> = emptyList(),
    val tagline: String? = null,
    val status: String? = null,
    val budget: Long? = 0,
    val revenue: Long? = 0,
    @SerialName("original_language")
    val originalLanguage: String? = null,
    @SerialName("original_title")
    val originalTitle: String? = null,
    val homepage: String? = null,
    @SerialName("imdb_id")
    val imdbId: String? = null,
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompanyDto> = emptyList(),
    @SerialName("production_countries")
    val productionCountries: List<ProductionCountryDto> = emptyList()
)

@Serializable
data class GenreDto(
    val id: Int,
    val name: String
)

@Serializable
data class ProductionCompanyDto(
    val id: Int,
    val name: String,
    @SerialName("logo_path")
    val logoPath: String? = null,
    @SerialName("origin_country")
    val originCountry: String? = null
)

@Serializable
data class ProductionCountryDto(
    @SerialName("iso_3166_1")
    val iso31661: String,
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
    val genres: List<Genre>,
    val tagline: String?,
    val status: String?,
    val budget: Long?,
    val revenue: Long?,
    val originalLanguage: String?,
    val homepage: String?,
    val imdbId: String?,
    val productionCompanies: List<ProductionCompany>,
    val productionCountries: List<ProductionCountry>
)

data class Genre(
    val id: Int,
    val name: String
)

data class ProductionCompany(
    val id: Int,
    val name: String,
    val logoPath: String?,
    val originCountry: String?
)

data class ProductionCountry(
    val iso31661: String,
    val name: String
)
