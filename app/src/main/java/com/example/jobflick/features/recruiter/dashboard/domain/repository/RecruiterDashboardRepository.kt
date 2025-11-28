package com.example.jobflick.features.recruiter.dashboard.domain.repository

import com.example.jobflick.features.recruiter.dashboard.domain.model.RecruiterDashboardStats
import com.example.jobflick.features.recruiter.dashboard.domain.model.RecruiterJobSummary

interface RecruiterDashboardRepository {
    suspend fun getActiveJobs(): List<RecruiterJobSummary>
    suspend fun getDashboardStats(): RecruiterDashboardStats
}
