package com.cinescope.movie.di

import com.cinescope.movie.domain.movie.MovieStateHolder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private object IosKoin : KoinComponent {
    val movieStateHolder: MovieStateHolder by inject()
}

fun getMovieStateHolder(): MovieStateHolder {
    return IosKoin.movieStateHolder
}