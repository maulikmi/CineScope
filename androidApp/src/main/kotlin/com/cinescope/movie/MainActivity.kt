package com.cinescope.movie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cinescope.movie.domain.movie.MovieStateHolder
import com.cinescope.movie.domain.movie.details.MovieDetailsStateHolder
import com.cinescope.movie.navigation.AppNavigation
import org.koin.android.ext.android.getKoin

class MainActivity : ComponentActivity() {
    val movieStateHolder: MovieStateHolder = getKoin().get()
    val movieDetailsStateHolder: MovieDetailsStateHolder = getKoin().get()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        movieStateHolder.loadPopularMovies()
        setContent {
            AppNavigation(
                movieStateHolder = movieStateHolder,
                movieDetailsStateHolder
            )
        }
    }
}

