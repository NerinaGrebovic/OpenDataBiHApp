package com.example.opendatabih.presentation.navigation

import LostDocumentDetailScreen
import StatisticsScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.opendatabih.presentation.components.TopBar
import com.example.opendatabih.presentation.screen.*
import com.example.opendatabih.di.provideLostDocumentsViewModel
import com.example.opendatabih.di.provideValidTravelDocumentsViewModel
import com.example.opendatabih.di.provideFavoriteViewModel
import androidx.compose.material3.Scaffold
import com.example.opendatabih.presentation.screen.favorite.FavoriteScreen
import com.example.opendatabih.presentation.screen.lost.LostDocumentsScreen
import com.example.opendatabih.presentation.screen.valid.ValidTravelDetailScreen
import com.example.opendatabih.presentation.screen.valid.ValidTravelDocumentsScreen

object NavRoutes {
    const val HOME = "home"
    const val LOST_DOCUMENTS = "lost_documents"
    const val VALID_TRAVEL = "valid_travel_documents"
    const val FAVORITES = "favorites"
    const val SPLASH = "splash"
    const val STATISTICS = "statistics"

    const val LOST_DOCUMENT_DETAIL = "lost_document_detail/{institution}"
    fun lostDocumentDetailRoute(institution: String) = "lost_document_detail/$institution"

    const val VALID_TRAVEL_DETAIL = "valid_travel_detail/{institution}"
    fun validTravelDetailRoute(institution: String) = "valid_travel_detail/$institution"
}

@Composable
fun AppNavGraph(navController: NavHostController, padding: androidx.compose.foundation.layout.PaddingValues) {

    val favoriteViewModel = provideFavoriteViewModel()
    val lostDocumentsViewModel = provideLostDocumentsViewModel()
    val validTravelDocumentsViewModel = provideValidTravelDocumentsViewModel()

    NavHost(navController = navController, startDestination = NavRoutes.SPLASH) {

        composable(NavRoutes.SPLASH) {
            SplashScreen(navController)
        }

        composable(NavRoutes.HOME) {
            HomeScreen(
                navController = navController,
                padding = padding,
                lostViewModel = lostDocumentsViewModel,
                validViewModel = validTravelDocumentsViewModel
            )
        }

        composable(NavRoutes.LOST_DOCUMENTS) {
            Scaffold(
                topBar = {
                    TopBar(title = "Izgubljeni dokumenti", onBackClick = { navController.popBackStack() })
                }
            ) { innerPadding ->
                LostDocumentsScreen(
                    viewModel = lostDocumentsViewModel,
                    navController = navController,
                    padding = innerPadding
                )
            }
        }

        composable(NavRoutes.VALID_TRAVEL) {
            Scaffold(
                topBar = {
                    TopBar(title = "Važeće putne isprave", onBackClick = { navController.popBackStack() })
                }
            ) { innerPadding ->
                ValidTravelDocumentsScreen(
                    viewModel = validTravelDocumentsViewModel,
                    navController = navController,
                    padding = innerPadding
                )
            }
        }

        composable(
            route = NavRoutes.LOST_DOCUMENT_DETAIL,
            arguments = listOf(navArgument("institution") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val institution = backStackEntry.arguments?.getString("institution") ?: ""

            Scaffold(
                topBar = {
                    TopBar(title = "Detalji dokumenta", onBackClick = { navController.popBackStack() })
                }
            ) { innerPadding ->
                LostDocumentDetailScreen(
                    institution = institution,
                    viewModel = lostDocumentsViewModel,
                    favoriteViewModel = favoriteViewModel,
                    padding = innerPadding
                )
            }
        }

        composable(
            route = NavRoutes.VALID_TRAVEL_DETAIL,
            arguments = listOf(navArgument("institution") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val institution = backStackEntry.arguments?.getString("institution") ?: ""

            Scaffold(
                topBar = {
                    TopBar(title = "Detalji dokumenta", onBackClick = { navController.popBackStack() })
                }
            ) { innerPadding ->
                ValidTravelDetailScreen(
                    institution = institution,
                    viewModel = validTravelDocumentsViewModel,
                    favoriteViewModel = favoriteViewModel,
                    padding = innerPadding
                )
            }
        }

        composable(NavRoutes.FAVORITES) {
            Scaffold(
                topBar = {
                    TopBar(title = "Vaši favoriti", onBackClick = { navController.popBackStack() })
                }
            ) { innerPadding ->
                FavoriteScreen(
                    favoriteViewModel = provideFavoriteViewModel(),
                    onLostDocumentClick = { favorite ->
                        navController.navigate(NavRoutes.lostDocumentDetailRoute(favorite.institution))
                    },
                    onValidDocumentClick = { favorite ->
                        navController.navigate(NavRoutes.validTravelDetailRoute(favorite.institution))
                    },
                    padding = innerPadding
                )
            }
        }



        composable(NavRoutes.STATISTICS) {
            Scaffold(
                topBar = {
                    TopBar(title = "Statistika", onBackClick = { navController.popBackStack() })
                }
            ) { innerPadding ->
                StatisticsScreen(
                    lostDocumentsViewModel = lostDocumentsViewModel,
                    validTravelDocumentsViewModel = validTravelDocumentsViewModel,
                    padding = innerPadding
                )
            }
        }
    }
}
