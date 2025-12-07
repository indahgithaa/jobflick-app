package com.example.jobflick.features.jobseeker.profile.domain.model

enum class JobCategory {
    MATCH,
    SAVED,
    APPLIED
}

data class Job(
    val id: String,
    val companyName: String,
    val companyLogoUrl: String?,
    val jobTitle: String,
    val location: String,
    val postedTimestamp: String,
    val level: String?,
    val salaryRange: String?,
    val workType: String?,
    val skills: List<String>,
    val category: JobCategory,
    val statusTimestamp: String,
    val description: String,
    val minimumQualifications: List<String>,
    val aboutCompany: String
)
