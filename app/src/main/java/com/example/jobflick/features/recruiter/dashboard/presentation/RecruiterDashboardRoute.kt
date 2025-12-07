package com.example.jobflick.features.recruiter.dashboard.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.jobflick.features.recruiter.dashboard.data.datasource.RecruiterDashboardRemoteDataSource
import com.example.jobflick.features.recruiter.dashboard.data.repository.RecruiterDashboardRepositoryImpl
import com.example.jobflick.features.recruiter.dashboard.presentation.screens.RecruiterDashboardScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecruiterDashboardRoute(
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    onAddJobClick: () -> Unit,
    onJobClick: (String) -> Unit
) {
    val repository = remember {
        RecruiterDashboardRepositoryImpl(
            remoteDataSource = RecruiterDashboardRemoteDataSource()
        )
    }
    val viewModel = remember { RecruiterDashboardViewModel(repository) }
    val uiState by viewModel.uiState

    RecruiterDashboardScreen(
        currentRoute = currentRoute,
        uiState = uiState,
        onTabSelected = onTabSelected,
        onAddJobClick = onAddJobClick,
        onJobClick = onJobClick,
        onRetry = { viewModel.loadDashboard() }
    )
}
