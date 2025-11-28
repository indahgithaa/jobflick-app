package com.example.jobflick.features.recruiter.postjob.domain.repository

import com.example.jobflick.features.recruiter.postjob.domain.model.RecruiterJobPostRequest

interface RecruiterJobPostRepository {
    suspend fun createJob(request: RecruiterJobPostRequest): Boolean
}
