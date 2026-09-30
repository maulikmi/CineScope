package com.cinescope.movie.di

import com.cinescope.movie.domain.movie.GetPopularMoviesUseCase
import com.cinescope.movie.network.TmdbApi
import com.cinescope.movie.network.createHttpClient
import com.cinescope.movie.domain.movie.MovieRepository
import com.cinescope.movie.domain.movie.MovieRepositoryImpl
import com.cinescope.movie.domain.movie.MovieStateHolder
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
    single<MovieRepository>{
        MovieRepositoryImpl(get())
    }
    single {
        GetPopularMoviesUseCase(get())
    }
    single {
        MovieStateHolder(get())
    }
}