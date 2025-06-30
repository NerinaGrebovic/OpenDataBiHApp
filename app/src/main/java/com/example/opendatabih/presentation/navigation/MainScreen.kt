package com.example.opendatabih.presentation.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.opendatabih.presentation.components.BottomBar

@Composable
fun MainScreen(navController: NavHostController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute == NavRoutes.HOME ||
            currentRoute == NavRoutes.FAVORITES ||
            currentRoute == NavRoutes.STATISTICS

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(navController = navController)
            }
        }
    ) { padding ->
        AppNavGraph(navController = navController, padding = padding)
    }
}
