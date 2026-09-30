package com.cinescope.movie.di

import org.koin.core.context.startKoin

fun initKoin(
    tmdbToken: String
) {
    startKoin {
        modules(
            appModule(tmdbToken)
        )
    }
}