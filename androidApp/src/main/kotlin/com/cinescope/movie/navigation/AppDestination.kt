package com.cinescope.movie.navigation

sealed class AppDestination(
    val route: String
) {
    data object Home : AppDestination("Home")
    data object Search : AppDestination("Search")
    data object Favorites : AppDestination("Favorites")
}