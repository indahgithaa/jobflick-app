package com.example.jobflick.features.recruiter.postjob.data.datasource

import com.example.jobflick.features.recruiter.postjob.domain.model.RecruiterJobPostRequest
import kotlinx.coroutines.delay

data class JobPostRequestDto(
    val title: String,
    val level: String,
    val jobType: String,
    val workSystem: String,
    val minSalary: Long?,
    val maxSalary: Long?,
    val isNegotiable: Boolean,
    val skills: List<String>,
    val description: String,
    val minQualifications: String
)

class RecruiterJobPostRemoteDataSource {

    // simulasi POST /recruiter/jobs
    suspend fun createJob(request: JobPostRequestDto): Boolean {
        delay(700) // simulasi network
        // nanti diisi logic success/failure dari response BE
        return true
    }

    companion object {
        fun fromDomain(domain: RecruiterJobPostRequest): JobPostRequestDto {
            return JobPostRequestDto(
                title = domain.title,
                level = domain.level,
                jobType = domain.jobType,
                workSystem = domain.workSystem,
                minSalary = domain.minSalary,
                maxSalary = domain.maxSalary,
                isNegotiable = domain.isNegotiable,
                skills = domain.skills,
                description = domain.description,
                minQualifications = domain.minQualifications
            )
        }
    }
}
