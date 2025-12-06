package com.example.jobflick.features.jobseeker.discover.data.datasource

import com.example.jobflick.core.network.PocketBaseHttp
import com.example.jobflick.features.jobseeker.discover.domain.model.JobPosting
import com.example.jobflick.features.jobseeker.profile.domain.model.JobCategory
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.jsonPrimitive

class DiscoverRemoteDataSource(
    private val pb: PocketBaseHttp,
    private val profilesCollection: String,
    private val jobsCollection: String,
    private val jobCategoriesCollection: String
) {

    suspend fun getDiscoverJobs(): List<JobPosting> {
        val authResponse = pb.authRefresh(collection = profilesCollection)
        val userId = authResponse.record.id

        // Fetch jobCategories for the user
        val jobCategoriesResponse = pb.getFullList(jobCategoriesCollection, filter = "user=\"$userId\"")
        val categorizedJobIds = jobCategoriesResponse.map { it.data["job"]?.jsonPrimitive?.content }.toSet()

        // Fetch all jobs
        val jobsResponse = pb.getFullList(jobsCollection)

        return jobsResponse.filter { record ->
            record.id !in categorizedJobIds
        }.map { record ->
            JobPosting(
                id = record.id,
                company = record.data["companyName"]?.jsonPrimitive?.content ?: "Unknown",
                logoUrl = record.data["companyLogoUrl"]?.jsonPrimitive?.content,
                location = record.data["location"]?.jsonPrimitive?.content ?: "Unknown",
                title = record.data["jobTitle"]?.jsonPrimitive?.content ?: "Unknown",
                postedAt = record.data["postedTimestamp"]?.jsonPrimitive?.content ?: "",
                level = record.data["level"]?.jsonPrimitive?.content ?: "Unknown",
                salary = record.data["salaryRange"]?.jsonPrimitive?.content ?: "Unknown",
                workType = record.data["workType"]?.jsonPrimitive?.content ?: "Unknown",
                skills = record.data["skills"]?.jsonPrimitive?.content?.split(";")?.map { it.trim() } ?: emptyList(),
                about = record.data["description"]?.jsonPrimitive?.content ?: ""
            )
        }
    }

    suspend fun getAllJobs(): List<JobPosting> {
        // Fetch all jobs
        val jobsResponse = pb.getFullList(jobsCollection)

        return jobsResponse.map { record ->
            JobPosting(
                id = record.id,
                company = record.data["companyName"]?.jsonPrimitive?.content ?: "Unknown",
                logoUrl = record.data["companyLogoUrl"]?.jsonPrimitive?.content,
                location = record.data["location"]?.jsonPrimitive?.content ?: "Unknown",
                title = record.data["jobTitle"]?.jsonPrimitive?.content ?: "Unknown",
                postedAt = record.data["postedTimestamp"]?.jsonPrimitive?.content ?: "",
                level = record.data["level"]?.jsonPrimitive?.content ?: "Unknown",
                salary = record.data["salaryRange"]?.jsonPrimitive?.content ?: "Unknown",
                workType = record.data["workType"]?.jsonPrimitive?.content ?: "Unknown",
                skills = record.data["skills"]?.jsonPrimitive?.content?.split(";")?.map { it.trim() } ?: emptyList(),
                about = record.data["description"]?.jsonPrimitive?.content ?: ""
            )
        }
    }

    suspend fun postJobCategory(jobId: String, category: JobCategory) {
        val authResponse = pb.authRefresh(collection = profilesCollection)
        val userId = authResponse.record.id

        val body = buildJsonObject {
            put("user", userId)
            put("job", jobId)
            put("category", category.name)
        }

        pb.create(jobCategoriesCollection, body)
    }
}
