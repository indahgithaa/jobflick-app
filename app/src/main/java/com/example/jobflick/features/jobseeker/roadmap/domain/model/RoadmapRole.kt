package com.example.jobflick.features.jobseeker.roadmap.domain.model

data class RoadmapRole(
    val id: String,                         // "senior_software_engineer"
    val name: String,                       // "Senior Software Engineer"
    val progress: Float,                    // 0f..1f
    val modules: List<RoadmapModule>,
    val availableRoles: List<String>,       // nama semua role untuk dropdown
    val updatedAt: String                   // "2024-11-27 10:19:00.123Z"
)
