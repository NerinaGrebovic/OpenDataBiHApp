package com.example.opendatabih.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Check

import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    object Home : BottomNavItem(
        route = NavRoutes.HOME,
        icon = Icons.Default.Home,
        title = "Home"
    )

    object Statistics : BottomNavItem(
        route = NavRoutes.STATISTICS,
        icon = Icons.Default.Check,
        title = "Statistika"
    )

    object Favorites : BottomNavItem(
        route = NavRoutes.FAVORITES,
        icon = Icons.Default.Favorite,
        title = "Favoriti"
    )

    companion object {
        val items = listOf(Home, Statistics, Favorites)
    }
}
