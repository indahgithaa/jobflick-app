package com.example.jobflick.features.recruiter.postjob.data.repository

import com.example.jobflick.features.recruiter.postjob.data.datasource.RecruiterJobPostRemoteDataSource
import com.example.jobflick.features.recruiter.postjob.data.datasource.RecruiterJobPostRemoteDataSource.Companion.fromDomain
import com.example.jobflick.features.recruiter.postjob.domain.model.RecruiterJobPostRequest
import com.example.jobflick.features.recruiter.postjob.domain.repository.RecruiterJobPostRepository

class RecruiterJobPostRepositoryImpl(
    private val remote: RecruiterJobPostRemoteDataSource
) : RecruiterJobPostRepository {

    override suspend fun createJob(request: RecruiterJobPostRequest): Boolean {
        val dto = fromDomain(request)
        return remote.createJob(dto)
    }
}
