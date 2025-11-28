package com.example.jobflick.features.jobseeker.discover.domain.repository

import com.example.jobflick.features.jobseeker.discover.domain.model.JobPosting
import com.example.jobflick.features.jobseeker.profile.domain.model.JobCategory

interface DiscoverRepository {
    suspend fun getDiscoverJobs(): List<JobPosting>
    suspend fun postJobCategory(jobId: String, category: JobCategory)
}
