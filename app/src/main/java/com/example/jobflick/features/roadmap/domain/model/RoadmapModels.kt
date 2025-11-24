package com.example.jobflick.features.roadmap.domain.model

import androidx.compose.ui.graphics.Color

data class RoadmapRole(
    val name: String,
    val progress: Float,
    val modules: List<RoadmapModule>,
    val availableRoles: List<String>
)

data class RoadmapModule(
    val number: Int,
    val title: String,
    val description: String,
    val progress: Float,
    val color: Color,
    val learningPoints: List<String>,
    val articles: List<String>,
    val quizTitle: String,
    val questions: List<QuizQuestion>
)

data class QuizQuestion(
    val question: String,
    val options: List<String>,
)
