package com.example.jobflick.features.auth.domain.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val profilePicture: String? = null,
    val role: String = "jobseeker"
)
