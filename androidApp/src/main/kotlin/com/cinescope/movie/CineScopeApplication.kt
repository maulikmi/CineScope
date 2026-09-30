package com.cinescope.movie

import android.app.Application
import com.cinescope.movie.di.initKoin

class CineScopeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin(
            tmdbToken = BuildConfig.TMDB_TOKEN
        )
    }
}