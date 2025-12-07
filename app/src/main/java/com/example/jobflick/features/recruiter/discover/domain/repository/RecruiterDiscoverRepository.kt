package com.example.jobflick.features.recruiter.discover.domain.repository

import com.example.jobflick.features.recruiter.discover.domain.model.CandidateProfile

interface RecruiterDiscoverRepository {
    suspend fun getCandidates(currentRole: String): List<CandidateProfile>
}
