package com.example.jobflick.features.jobseeker.roadmap.domain.model

import androidx.compose.ui.graphics.Color

data class RoadmapModule(
    val id: String,                         // "senior_se_mod1"
    val number: Int,                        // 1, 2, 3, ...
    val title: String,
    val description: String,
    val progress: Float,                    // 0f..1f
    val color: Color,                       // untuk UI card
    val learningPoints: List<String>,
    val articles: List<RoadmapArticle>,
    val quizTitle: String,
    val questions: List<QuizQuestion>,
    val status: RoadmapModuleStatus,        // COMPLETED / IN_PROGRESS / LOCKED
    val updatedAt: String                   // "2024-11-27 10:17:00.123Z"
)
