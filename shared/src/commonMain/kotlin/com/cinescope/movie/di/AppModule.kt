package com.cinescope.movie.di

import com.cinescope.movie.domain.movie.GetPopularMoviesUseCase
import com.cinescope.movie.domain.movie.MovieRepository
import com.cinescope.movie.domain.movie.MovieRepositoryImpl
import com.cinescope.movie.domain.movie.MovieStateHolder
import com.cinescope.movie.domain.movie.details.GetMovieDetailsUseCase
import com.cinescope.movie.domain.movie.details.MovieDetailsStateHolder
import com.cinescope.movie.domain.movie.search.SearchMoviesUseCase
import com.cinescope.movie.domain.movie.search.SearchStateHolder
import com.cinescope.movie.network.TmdbApi
import com.cinescope.movie.network.createHttpClient
import org.koin.dsl.module

fun appModule(
    tmdbToken: String
) = module {
    single {
        createHttpClient(tmdbToken)
    }
    single {
        TmdbApi(get())
    }
    single<MovieRepository> {
        MovieRepositoryImpl(get())
    }

    // Use cases
    single {
        GetPopularMoviesUseCase(get())
    }
    single {
        GetMovieDetailsUseCase(get())
    }
    factory { SearchMoviesUseCase(get()) }

    // StateHolders
    factory {
        MovieStateHolder(get())
    }
    factory {
        MovieDetailsStateHolder(get())
    }
    factory { SearchStateHolder(get()) }
}