package com.example.jobflick.features.recruiter.dashboard.domain.model

/**
 * Statistik agregat untuk kartu-kartu di bawah dashboard.
 *
 * lastUpdatedText disimpan sebagai string agar BE bisa langsung kirim
 * misal "30 Maret 2025" tanpa perlu formatting di FE.
 */
data class RecruiterDashboardStats(
    val totalJobs: Int,
    val totalApplicants: Int,
    val totalInterviews: Int,
    val totalAccepted: Int,
    val lastUpdatedText: String
)
