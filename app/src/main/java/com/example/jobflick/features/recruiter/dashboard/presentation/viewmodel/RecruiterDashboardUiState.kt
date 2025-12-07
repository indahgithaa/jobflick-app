package com.example.jobflick.features.recruiter.dashboard.presentation

import com.example.jobflick.features.recruiter.dashboard.domain.model.RecruiterDashboardStats
import com.example.jobflick.features.recruiter.dashboard.domain.model.RecruiterJobSummary

data class RecruiterDashboardUiState(
    val isLoading: Boolean = true,
    val activeJobs: List<RecruiterJobSummary> = emptyList(),
    val stats: RecruiterDashboardStats? = null,
    val errorMessage: String? = null
)
