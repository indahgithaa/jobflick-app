package com.example.jobflick.features.recruiter.discover.domain.usecase

import com.example.jobflick.features.recruiter.discover.domain.model.CandidateProfile
import com.example.jobflick.features.recruiter.discover.domain.repository.RecruiterDiscoverRepository

class GetRecruiterCandidatesUseCase(
    private val repository: RecruiterDiscoverRepository
) {
    suspend operator fun invoke(roleName: String): List<CandidateProfile> {
        return repository.getCandidates(roleName)
    }
}
