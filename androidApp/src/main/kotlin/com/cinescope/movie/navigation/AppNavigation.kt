package com.cinescope.movie.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cinescope.movie.screens.FavoritesScreen
import com.cinescope.movie.screens.HomeScreen
import com.cinescope.movie.screens.MovieDetailsScreen
import com.cinescope.movie.screens.SearchScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute =
        backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar() {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.destination.route,
                        onClick = {
                            navController.navigate(item.destination.route) {
                                popUpTo(
                                    navController.graph.startDestinationId
                                ) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(item.icon),
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(item.label)
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(AppDestination.Home.route) {
                HomeScreen(
                ) { movieId ->
                    navController.navigate(
                        AppDestination.MovieDetails.createRoute(movieId)
                    )
                }
            }

            composable(AppDestination.Search.route) {
                SearchScreen(
                    onMovieClick = { movieId ->
                        navController.navigate(
                            AppDestination.MovieDetails.createRoute(movieId)
                        )
                    }
                )
            }

            composable(AppDestination.Favorites.route) {
                FavoritesScreen()
            }

            composable(
                route = AppDestination.MovieDetails.route,
                arguments = listOf(
                    navArgument("movieId") {
                        type = NavType.IntType
                    }
                )) { backStackEntry ->

                val movieId =
                    backStackEntry.arguments?.getInt("movieId")
                        ?: return@composable

                MovieDetailsScreen(
                    movieId = movieId,
                ) {
                    navController.popBackStack()
                }
            }
        }
    }
}