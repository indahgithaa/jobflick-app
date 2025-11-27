package com.example.jobflick.features.jobseeker.discover.domain.model

data class JobPosting(
    val id: String,
    val company: String,
    val location: String,
    val title: String,
    val workType: String,
    val skills: List<String>,
    val level: String,
    val salary: String,
    val about: String,
    val postedAt: String?,       // ISO-8601 timestamp
    val logoUrl: String? = null  // <-- URI/URL LOGO
)
