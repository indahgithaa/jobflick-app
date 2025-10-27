package com.example.prak_papb_6.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.prak_papb_6.ui.screens.DetailScreen
import com.example.prak_papb_6.ui.screens.HomeScreen
import com.example.prak_papb_6.ui.screens.ProfileScreen

object Destinations {
    const val HOME = "home"
    const val DETAIL = "detail/{message}"
    const val PROFILE = "profile"
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Destinations.HOME) {
        // ProfileScreen diletakkan sebelum HomeScreen
        composable(Destinations.PROFILE) {
            ProfileScreen(navController)
        }

        composable(Destinations.HOME) {
            HomeScreen(navController)
        }

        composable(
            route = Destinations.DETAIL,
            arguments = listOf(navArgument("message") { type = NavType.StringType })
        ) { backStackEntry ->
            DetailScreen(backStackEntry.arguments?.getString("message"))
        }
    }
}

