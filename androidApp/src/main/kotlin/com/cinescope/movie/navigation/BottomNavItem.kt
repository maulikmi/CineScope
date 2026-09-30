package com.cinescope.movie.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import com.cinescope.movie.R

data class BottomNavItem(
    val destination: AppDestination,
    val label: String,
    val icon: Int
)

val bottomNavItems = listOf(
    BottomNavItem(
        destination = AppDestination.Home,
        label = "Home",
        icon = R.drawable.ic_home
    ),
    BottomNavItem(
        destination = AppDestination.Search,
        label = "Search",
        icon = R.drawable.ic_search
    ),
    BottomNavItem(
        destination = AppDestination.Favorites,
        label = "Favorites",
        icon = R.drawable.ic_fav
    )
)