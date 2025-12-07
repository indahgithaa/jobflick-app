package com.example.jobflick.features.recruiter.dashboard.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.jobflick.features.recruiter.dashboard.data.datasource.DashboardStatsDto
import com.example.jobflick.features.recruiter.dashboard.data.datasource.JobSummaryDto
import com.example.jobflick.features.recruiter.dashboard.data.datasource.RecruiterDashboardRemoteDataSource
import com.example.jobflick.features.recruiter.dashboard.domain.model.RecruiterDashboardStats
import com.example.jobflick.features.recruiter.dashboard.domain.model.RecruiterJobSummary
import com.example.jobflick.features.recruiter.dashboard.domain.repository.RecruiterDashboardRepository

class RecruiterDashboardRepositoryImpl(
    private val remoteDataSource: RecruiterDashboardRemoteDataSource
) : RecruiterDashboardRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getActiveJobs(): List<RecruiterJobSummary> {
        return remoteDataSource
            .getActiveJobs()
            .filter { it.status == "ACTIVE" } // jika nanti BE kirim status lain
            .map { it.toDomain() }
    }

    override suspend fun getDashboardStats(): RecruiterDashboardStats {
        return remoteDataSource
            .getDashboardStats()
            .toDomain()
    }
}

// ============ Mapper DTO -> Domain ============

private fun JobSummaryDto.toDomain(): RecruiterJobSummary {
    return RecruiterJobSummary(
        id = id,
        title = title,
        city = city,
        createdAt = createdAt,            // String ISO, cocok dgn String?.toTimeAgoLabel()
        applicantsCount = applicantsCount,
        viewsCount = viewsCount
    )
}

private fun DashboardStatsDto.toDomain(): RecruiterDashboardStats {
    return RecruiterDashboardStats(
        totalJobs = totalJobs,
        totalApplicants = totalApplicants,
        totalInterviews = totalInterviews,
        totalAccepted = totalAccepted,
        lastUpdatedText = lastUpdatedText
    )
}
