package com.example.jobflick.features.jobseeker.roadmap.domain.model

data class QuizQuestion(
    val id: String,                 // id ala PocketBase, contoh: "mod1_q1"
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int     // index jawaban benar (0-based)
)
