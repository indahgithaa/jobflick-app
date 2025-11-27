package com.example.jobflick.features.jobseeker.discover.domain.usecase

import com.example.jobflick.features.jobseeker.discover.domain.model.JobPosting
import com.example.jobflick.features.jobseeker.discover.domain.repository.DiscoverRepository

class GetDiscoverJobsUseCase(
    private val repository: DiscoverRepository
) {
    suspend operator fun invoke(): List<JobPosting> = repository.getDiscoverJobs()
}
