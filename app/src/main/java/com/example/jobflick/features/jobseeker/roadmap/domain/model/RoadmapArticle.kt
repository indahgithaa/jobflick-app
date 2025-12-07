package com.example.jobflick.features.jobseeker.roadmap.domain.model

data class RoadmapArticle(
    val id: String,                 // contoh: "mod1_art1"
    val title: String,
    val content: String,
    val order: Int,
    val updatedAt: String           // format ala PocketBase: "2024-11-27 10:15:30.123Z"
)
