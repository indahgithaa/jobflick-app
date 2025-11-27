package com.example.jobflick.features.jobseeker.discover.data.repository

import com.example.jobflick.features.jobseeker.discover.data.datasource.DiscoverRemoteDataSource
import com.example.jobflick.features.jobseeker.discover.domain.model.JobPosting
import com.example.jobflick.features.jobseeker.discover.domain.repository.DiscoverRepository

class DiscoverRepositoryImpl(
    private val remoteDataSource: DiscoverRemoteDataSource
) : DiscoverRepository {

    override suspend fun getDiscoverJobs(): List<JobPosting> {
        return remoteDataSource.getDiscoverJobs()
    }
}
