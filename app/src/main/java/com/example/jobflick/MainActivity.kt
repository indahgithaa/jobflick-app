package com.example.jobflick

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jobflick.core.ui.components.BottomNavBar
import com.example.jobflick.core.ui.theme.AppTheme
import com.example.jobflick.navigation.NavGraph
import com.example.jobflick.navigation.Routes

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route

                // daftar screen yang pakai bottom navbar
                val bottomBarRoutes = listOf(
                    Routes.DISCOVER,
                    Routes.ROADMAP,
                    Routes.MESSAGE,
                    Routes.PROFILE,
                )

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            if (currentRoute in bottomBarRoutes) {
                                BottomNavBar(
                                    currentRoute = currentRoute,
                                    onItemSelected = { targetRoute ->
                                        if (currentRoute == targetRoute) return@BottomNavBar

                                        navController.navigate(targetRoute) {
                                            // biar stack-nya rapi & state tiap tab bisa disimpan
                                            popUpTo(Routes.DISCOVER) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    ) { innerPadding ->
                        NavGraph(
                            navController = navController,
                            modifier = Modifier.fillMaxSize().padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
