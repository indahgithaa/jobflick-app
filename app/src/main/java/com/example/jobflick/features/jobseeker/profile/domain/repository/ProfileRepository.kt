package com.example.jobflick.features.jobseeker.profile.domain.repository

import com.example.jobflick.features.jobseeker.profile.domain.model.*

interface ProfileRepository {
    suspend fun getProfile(): Profile
    suspend fun getJobs(category: JobCategory): List<Job>
    suspend fun getJobDetail(id: String): Job?
}
