package com.example.jobflick.features.recruiter.discover.domain.model

data class CandidateProfile(
    val id: String,
    val name: String,
    val headline: String,            // Software Engineer
    val scorePercentage: Int,        // 80 -> 80% kecocokan
    val photoUrl: String?,           // URL foto kandidat (nullable)
    val level: String,               // Junior / Mid / Senior
    val availability: String,        // Langsung mulai, 1 bulan lagi, dll
    val location: String,            // Jakarta Selatan
    val education: String,           // S1 Informatika
    val experiences: List<CandidateExperience>,
    val skills: List<String>,
    val certifications: List<String>,
    val languages: List<String>,     // "Indonesia (Fasih)", "English (Menengah)"
    val jobTypes: List<String>,      // Full time, freelance
    val workSystems: List<String>,   // Hybrid, remote
    val expectedSalary: String,      // "Rp8.000.000"
    val email: String,
    val phone: String,
    val cvUrl: String?,              // URL CV
    val portfolioUrl: String?        // URL portofolio
)

data class CandidateExperience(
    val role: String,
    val company: String,
    val period: String,              // "2022—2024"
    val description: String
)
