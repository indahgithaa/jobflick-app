package com.example.jobflick.features.recruiter.dashboard.domain.model

/**
 * Ringkasan 1 lowongan untuk recruiter dashboard.
 *
 * createdAt: String ISO-8601 (OffsetDateTime),
 * contoh: "2024-11-23T02:14:00.123456+00:00"
 * -> langsung kompatibel dengan String?.toTimeAgoLabel()
 */
data class RecruiterJobSummary(
    val id: String,
    val title: String,
    val city: String,
    val createdAt: String,
    val applicantsCount: Int,
    val viewsCount: Int
)
