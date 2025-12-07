package com.example.jobflick.features.recruiter.postjob.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.jobflick.features.recruiter.postjob.data.datasource.RecruiterJobPostRemoteDataSource
import com.example.jobflick.features.recruiter.postjob.data.repository.RecruiterJobPostRepositoryImpl
import com.example.jobflick.features.recruiter.postjob.presentation.screens.RecruiterPostJobScreen

@Composable
fun RecruiterPostJobRoute(
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    onBack: () -> Unit,
    onSubmitSuccess: () -> Unit
) {
    val repository = remember { RecruiterJobPostRepositoryImpl(RecruiterJobPostRemoteDataSource()) }
    val viewModel = remember { RecruiterPostJobViewModel(repository) }
    val uiState by viewModel.uiState

    RecruiterPostJobScreen(
        currentRoute = currentRoute,
        uiState = uiState,
        viewModel = viewModel,
        onTabSelected = onTabSelected,
        onBack = onBack,
        onSubmitSuccess = onSubmitSuccess
    )
}
