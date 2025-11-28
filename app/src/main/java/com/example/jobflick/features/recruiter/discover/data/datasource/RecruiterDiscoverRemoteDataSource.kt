package com.example.jobflick.features.recruiter.discover.data.datasource

import com.example.jobflick.features.recruiter.discover.domain.model.CandidateExperience
import com.example.jobflick.features.recruiter.discover.domain.model.CandidateProfile
import kotlinx.coroutines.delay

/**
 * Dummy remote data source.
 * Nanti tinggal diganti ke PocketBase / REST API:
 *   GET /candidates?role=Junior%20Software%20Engineer
 */
class RecruiterDiscoverRemoteDataSource {

    suspend fun getCandidatesForRole(roleName: String): List<CandidateProfile> {
        // Simulasi network delay
        delay(500)

        val headline = roleName.ifBlank { "Software Engineer" }

        return listOf(
            CandidateProfile(
                id = "candidate_001",
                name = "Aulia Rahma",
                headline = headline,
                scorePercentage = 80,
                photoUrl = null,
                level = "Junior",
                availability = "Langsung mulai",
                location = "Jakarta Selatan",
                education = "S1 Informatika",
                experiences = listOf(
                    CandidateExperience(
                        role = "Software Engineer",
                        company = "PT Bank Berdikari Asia",
                        period = "2022—2024",
                        description = "Bertanggung jawab mengembangkan sistem backend, " +
                                "memastikan keamanan aplikasi, dan mengelola integrasi API untuk layanan perbankan digital."
                    ),
                    CandidateExperience(
                        role = "Full-Stack Developer",
                        company = "PT Shopedia Jaya Abadi",
                        period = "2021—2022",
                        description = "Mengembangkan fitur e-commerce dan berkolaborasi dengan tim UI/UX."
                    )
                ),
                skills = listOf("Java", "C++", "Python", "JavaScript", "MySQL", "PostgreSQL"),
                certifications = listOf(
                    "Full-Stack Developer, Dicoding Indonesia (2019)",
                    "Software Engineer, Dicoding Indonesia (2019)"
                ),
                languages = listOf("Bahasa Indonesia (Fasih)", "English (Menengah)"),
                jobTypes = listOf("Full time", "Freelance"),
                workSystems = listOf("Hybrid", "Remote"),
                expectedSalary = "Rp8.000.000",
                email = "auliarahma@gmail.com",
                phone = "+62 81234567890",
                cvUrl = "https://example.com/cv/aulia.pdf",
                portfolioUrl = "https://example.com/portfolio/aulia"
            ),
            CandidateProfile(
                id = "candidate_002",
                name = "Bima Pratama",
                headline = headline,
                scorePercentage = 75,
                photoUrl = null,
                level = "Junior",
                availability = "1 bulan lagi",
                location = "Bandung",
                education = "S1 Teknik Informatika",
                experiences = listOf(
                    CandidateExperience(
                        role = "Backend Developer",
                        company = "PT Nusantara Digital",
                        period = "2021—2024",
                        description = "Mengembangkan REST API, menulis unit test, dan optimasi query database."
                    )
                ),
                skills = listOf("Kotlin", "Spring Boot", "PostgreSQL", "Docker"),
                certifications = listOf("Backend Developer, Dicoding Indonesia (2020)"),
                languages = listOf("Bahasa Indonesia (Fasih)", "English (Menengah)"),
                jobTypes = listOf("Full time"),
                workSystems = listOf("Remote"),
                expectedSalary = "Rp9.000.000",
                email = "bima.pratama@example.com",
                phone = "+62 81300001111",
                cvUrl = "https://example.com/cv/bima.pdf",
                portfolioUrl = "https://example.com/portfolio/bima"
            ),
            CandidateProfile(
                id = "candidate_003",
                name = "Citra Lestari",
                headline = headline,
                scorePercentage = 70,
                photoUrl = null,
                level = "Junior",
                availability = "Segera",
                location = "Surabaya",
                education = "S1 Sistem Informasi",
                experiences = listOf(
                    CandidateExperience(
                        role = "Front-End Developer",
                        company = "PT Kreatif Teknologi",
                        period = "2022—2024",
                        description = "Mengembangkan UI menggunakan React, integrasi dengan API, dan bekerja sama dengan tim desain."
                    )
                ),
                skills = listOf("JavaScript", "React", "TypeScript", "Figma"),
                certifications = listOf("Front-End Web Developer, Dicoding Indonesia (2021)"),
                languages = listOf("Bahasa Indonesia (Fasih)", "English (Dasar)"),
                jobTypes = listOf("Full time", "Part time"),
                workSystems = listOf("Hybrid"),
                expectedSalary = "Rp7.500.000",
                email = "citra.lestari@example.com",
                phone = "+62 81500002222",
                cvUrl = "https://example.com/cv/citra.pdf",
                portfolioUrl = "https://example.com/portfolio/citra"
            )
        )
    }
}
