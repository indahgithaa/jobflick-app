package com.example.jobflick.features.recruiter.postjob.domain.model

data class RecruiterJobPostRequest(
    val title: String,
    val level: String,
    val jobType: String,
    val workSystem: String,
    val minSalary: Long?,
    val maxSalary: Long?,
    val isNegotiable: Boolean,
    val skills: List<String>,
    val description: String,
    val minQualifications: String
)
