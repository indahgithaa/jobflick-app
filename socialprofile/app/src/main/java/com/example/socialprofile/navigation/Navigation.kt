package com.example.socialprofile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onOpenDetail = {
                    navController.navigate("detail")
                }
            )
        }

        composable("detail") {
            DetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}


}
