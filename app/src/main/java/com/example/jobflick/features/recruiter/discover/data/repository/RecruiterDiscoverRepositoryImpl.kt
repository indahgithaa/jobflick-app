package com.example.jobflick.features.recruiter.discover.data.repository

import com.example.jobflick.features.recruiter.discover.data.datasource.RecruiterDiscoverRemoteDataSource
import com.example.jobflick.features.recruiter.discover.domain.model.CandidateProfile
import com.example.jobflick.features.recruiter.discover.domain.repository.RecruiterDiscoverRepository

class RecruiterDiscoverRepositoryImpl(
    private val remoteDataSource: RecruiterDiscoverRemoteDataSource
) : RecruiterDiscoverRepository {

    override suspend fun getCandidates(currentRole: String): List<CandidateProfile> {
        // Satu pintu ke remote. Nanti kalau ada cache/local tinggal ditambah di sini.
        return remoteDataSource.getCandidatesForRole(currentRole)
    }
}
