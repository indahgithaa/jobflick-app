package com.example.jobflick.features.jobseeker.discover.domain.model

/**
 * Model job untuk fitur Discover (jobseeker).
 * Nanti ini bisa langsung dipetakan dari response BE.
 */
data class JobPosting(
    val id: String,
    val company: String,
    val logoUrl: String?,          // URL logo perusahaan (bisa null)
    val location: String,
    val title: String,
    val postedAt: String,          // ISO-8601: "2024-11-24T02:14:00.123456+00:00"
    val level: String,
    val salary: String,
    val workType: String,
    val skills: List<String>,
    val about: String              // deskripsi / tentang perusahaan (sementara 1 field)
)
