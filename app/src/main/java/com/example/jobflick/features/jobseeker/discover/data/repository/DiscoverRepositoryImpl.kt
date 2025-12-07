package com.example.jobflick.features.jobseeker.discover.data.repository

import com.example.jobflick.features.jobseeker.discover.data.datasource.DiscoverRemoteDataSource
import com.example.jobflick.features.jobseeker.discover.domain.model.JobPosting
import com.example.jobflick.features.jobseeker.discover.domain.repository.DiscoverRepository
import com.example.jobflick.features.jobseeker.profile.domain.model.JobCategory

class DiscoverRepositoryImpl(
    private val remoteDataSource: DiscoverRemoteDataSource
) : DiscoverRepository {

    override suspend fun getDiscoverJobs(): List<JobPosting> {
        return remoteDataSource.getDiscoverJobs()
    }

    override suspend fun postJobCategory(jobId: String, category: JobCategory) {
        return remoteDataSource.postJobCategory(jobId, category)
    }
}
