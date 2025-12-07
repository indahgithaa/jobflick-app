package com.example.jobflick.features.jobseeker.profile.domain.usecases

import com.example.jobflick.features.jobseeker.profile.domain.model.JobCategory
import com.example.jobflick.features.jobseeker.profile.domain.repository.ProfileRepository

class GetProfileUseCase(
    private val repo: ProfileRepository
) {
    suspend operator fun invoke() = repo.getProfile()
}

class GetJobsUseCase(
    private val repo: ProfileRepository
) {
    suspend operator fun invoke(category: JobCategory) =
        repo.getJobs(category)
}

class GetJobDetailUseCase(
    private val repo: ProfileRepository
) {
    suspend operator fun invoke(id: String) =
        repo.getJobDetail(id)
}
