package com.example.jobflick.features.profile.data.datasource

import com.example.jobflick.features.profile.domain.model.Job
import com.example.jobflick.features.profile.domain.model.JobCategory
import com.example.jobflick.features.profile.domain.model.Profile
import kotlinx.coroutines.delay

class ProfileRemoteDataSource {

    suspend fun getProfile(): Profile {
        delay(300)

        return Profile(
            id = "user_01",
            name = "Aulia Rahma",
            photoUrl = null // gunakan foto default di UI
        )
    }

    /**
     * Mengambil semua job dari server (dummy data).
     * Nanti bisa diganti dengan PocketBaseHttp.getFullList("jobs")
     */
    suspend fun getJobs(): List<Job> {
        delay(400)

        val description = """
            Bertanggung jawab dalam proses desain dan pengembangan perangkat lunak 
            dengan fokus pada performa, skalabilitas, dan kualitas sistem.
            Bekerja sama dengan tim lintas fungsi untuk mendukung layanan berskala besar.
        """.trimIndent()

        val qualifications = listOf(
            "Lulusan S1 terkait bidang teknologi informasi.",
            "Memiliki pengalaman minimal 1 tahun sebagai Software Engineer.",
            "Memahami prinsip dasar pembuatan layanan dan API."
        )

        val aboutCompany = """
            Microsoft adalah perusahaan teknologi global yang berfokus pada inovasi 
            dan pengembangan solusi berbasis cloud untuk meningkatkan produktivitas.
        """.trimIndent()

        return listOf(
            // MATCH
            Job(
                id = "aws-se-1",
                companyName = "Amazon Web Services",
                companyLogoUrl = null,
                jobTitle = "Software Engineer",
                location = "Jakarta, Indonesia",
                postedDaysAgo = 3, // diposting 3 hari lalu
                level = "Junior",
                salaryRange = "Rp8–10 juta/bulan",
                workType = "Full time, Remote",
                skills = listOf("Java", "Python", "AWS"),
                statusLabel = "Match",
                category = JobCategory.MATCH,
                dateText = "2 hari lalu",
                description = description,
                minimumQualifications = qualifications,
                aboutCompany = aboutCompany
            ),

            // MATCH 2
            Job(
                id = "tokped-be-1",
                companyName = "Tokopedia",
                companyLogoUrl = null,
                jobTitle = "Back-End Developer",
                location = "Jakarta, Indonesia",
                postedDaysAgo = 5,
                level = "Junior",
                salaryRange = "Rp9–11 juta/bulan",
                workType = "Full time",
                skills = listOf("Go", "Kotlin", "Microservices"),
                statusLabel = "Wawancara",
                category = JobCategory.MATCH,
                dateText = "2 hari lalu",
                description = description,
                minimumQualifications = qualifications,
                aboutCompany = aboutCompany
            ),

            // SAVED
            Job(
                id = "google-se-1",
                companyName = "Google",
                companyLogoUrl = null,
                jobTitle = "Software Engineer",
                location = "Jakarta, Indonesia",
                postedDaysAgo = 0, // hari ini
                level = "Junior",
                salaryRange = "Rp10–15 juta/bulan",
                workType = "Full time",
                skills = listOf("Kotlin", "Java", "Distributed Systems"),
                statusLabel = null,
                category = JobCategory.SAVED,
                dateText = "2 hari lalu",
                description = description,
                minimumQualifications = qualifications,
                aboutCompany = aboutCompany
            ),

            // APPLIED
            Job(
                id = "ms-jr-se-1",
                companyName = "Microsoft",
                companyLogoUrl = null,
                jobTitle = "Junior Software Engineer",
                location = "Jakarta, Indonesia",
                postedDaysAgo = 2,
                level = "Junior",
                salaryRange = "Rp8–10 juta/bulan",
                workType = "Full time, Remote",
                skills = listOf("C++", "JavaScript", "C#", "Python"),
                statusLabel = "Diproses",
                category = JobCategory.APPLIED,
                dateText = "2 hari lalu",
                description = description,
                minimumQualifications = qualifications,
                aboutCompany = aboutCompany
            )
        )
    }

    suspend fun getJobDetail(id: String): Job? {
        return getJobs().firstOrNull { it.id == id }
    }
}
