package com.example.jobflick.features.recruiter.dashboard.data.datasource

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.delay
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * DTO seolah-olah ini JSON dari BE.
 */
data class JobSummaryDto(
    val id: String,
    val title: String,
    val city: String,
    val createdAt: String,      // contoh: "2024-11-23T02:14:00.123456+00:00"
    val applicantsCount: Int,
    val viewsCount: Int,
    val status: String          // contoh: "ACTIVE", "CLOSED"
)

data class DashboardStatsDto(
    val totalJobs: Int,
    val totalApplicants: Int,
    val totalInterviews: Int,
    val totalAccepted: Int,
    val lastUpdatedText: String
)

class RecruiterDashboardRemoteDataSource {

    /**
     * Simulasi endpoint:
     * GET /recruiter/dashboard/active-jobs
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getActiveJobs(): List<JobSummaryDto> {
        delay(400) // simulasi latency

        val now = OffsetDateTime.now()
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        return listOf(
            JobSummaryDto(
                id = "job_01",
                title = "UI/UX Designer",
                city = "Jakarta",
                createdAt = now.minusDays(2).format(formatter),  // 2 hari lalu
                applicantsCount = 4,
                viewsCount = 120,
                status = "ACTIVE"
            ),
            JobSummaryDto(
                id = "job_02",
                title = "Front-End Developer",
                city = "Bandung",
                createdAt = now.minusDays(4).format(formatter),  // 4 hari lalu
                applicantsCount = 20,
                viewsCount = 234,
                status = "ACTIVE"
            ),
            JobSummaryDto(
                id = "job_03",
                title = "Data Scientist",
                city = "Surabaya",
                createdAt = now.minusDays(7).format(formatter),  // 7 hari lalu
                applicantsCount = 50,
                viewsCount = 312,
                status = "ACTIVE"
            ),
            JobSummaryDto(
                id = "job_04",
                title = "Data Engineer",
                city = "Jakarta",
                createdAt = now.minusDays(9).format(formatter),  // 9 hari lalu
                applicantsCount = 50,
                viewsCount = 480,
                status = "ACTIVE"
            )
        )
    }

    /**
     * Simulasi endpoint:
     * GET /recruiter/dashboard/stats
     */
    suspend fun getDashboardStats(): DashboardStatsDto {
        delay(250)

        return DashboardStatsDto(
            totalJobs = 10,
            totalApplicants = 124,
            totalInterviews = 17,
            totalAccepted = 2,
            lastUpdatedText = "30 Maret 2025"
        )
    }
}
