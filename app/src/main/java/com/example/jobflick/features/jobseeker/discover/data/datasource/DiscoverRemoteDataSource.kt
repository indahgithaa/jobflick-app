package com.example.jobflick.features.jobseeker.discover.data.datasource

import com.example.jobflick.features.jobseeker.discover.domain.model.JobPosting
import kotlinx.coroutines.delay

class DiscoverRemoteDataSource {

    suspend fun getDiscoverJobs(): List<JobPosting> {
        delay(400)

        val desc = """
            Bertanggung jawab dalam proses desain dan pengembangan perangkat lunak 
            dengan fokus pada performa, skalabilitas, dan kualitas sistem.
        """.trimIndent()

        val nowMs = "2024-11-24T02:14:00.123456+00:00"
        val nowGj = "2024-11-23T02:14:00.123456+00:00"
        val nowTs = "2024-11-24T05:44:00.123456+00:00"

        return listOf(
            JobPosting(
                id = "ms-jr-se-1",
                company = "Microsoft",
                logoUrl = "https://logo.clearbit.com/microsoft.com",
                location = "Jakarta, Indonesia",
                title = "Junior Software Engineer",
                postedAt = nowMs,
                level = "Junior",
                salary = "Rp8–10 jt/bln",
                workType = "Full time, Remote",
                skills = listOf("C++", "JavaScript", "C#", "Python"),
                about = desc
            ),
            JobPosting(
                id = "gj-lead-se-1",
                company = "Gojek",
                logoUrl = "https://logo.clearbit.com/gojek.com",
                location = "Jakarta, Indonesia",
                title = "Lead Software Engineer",
                postedAt = nowGj,
                level = "Mid",
                salary = "Rp9–12 jt/bln",
                workType = "Full time, Hybrid",
                skills = listOf("Golang", "Java", "SQL", "Clojure", "Ruby"),
                about = desc
            ),
            JobPosting(
                id = "startup-backend-1",
                company = "Tech Startup",
                logoUrl = null, // default icon dipakai
                location = "Bandung, Indonesia",
                title = "Backend Engineer",
                postedAt = nowTs,
                level = "Junior",
                salary = "Rp6–8 jt/bln",
                workType = "Full time, On-site",
                skills = listOf("Node.js", "PostgreSQL", "Docker", "Kubernetes"),
                about = desc
            )
        )
    }
}
