package com.example.jobflick.features.profile.data.repository

import com.example.jobflick.features.profile.data.datasource.ProfileRemoteDataSource
import com.example.jobflick.features.profile.domain.model.Job
import com.example.jobflick.features.profile.domain.model.JobCategory
import com.example.jobflick.features.profile.domain.model.Profile
import com.example.jobflick.features.profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val remote: ProfileRemoteDataSource
) : ProfileRepository {

    override suspend fun getProfile(): Profile =
        remote.getProfile()

    override suspend fun getJobs(category: JobCategory): List<Job> =
        remote.getJobs().filter { it.category == category }

    override suspend fun getJobDetail(id: String): Job? =
        remote.getJobs().firstOrNull { it.id == id }
}
