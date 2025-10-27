package com.example.jobflick.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jobflick.features.onboarding.presentation.OnboardingScreen
import com.example.jobflick.features.onboarding.presentation.SplashScreen
import com.example.jobflick.features.onboarding.presentation.RoleSelectionScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Routes.SPLASH
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigateNext = {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Routes.SELECTROLE) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.SELECTROLE) {
            RoleSelectionScreen(
                onJobSeeker = { /* TODO: nav ke flow jobseeker */ },
                onRecruiter = { /* TODO: nav ke flow recruiter */ }
            )
        }
    }
}
