package com.example.jobflick.features.jobseeker.discover.domain.repository

import com.example.jobflick.features.jobseeker.discover.domain.model.JobPosting

interface DiscoverRepository {
    suspend fun getDiscoverJobs(): List<JobPosting>
}
