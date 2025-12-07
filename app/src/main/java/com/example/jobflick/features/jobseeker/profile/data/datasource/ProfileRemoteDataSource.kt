package com.example.jobflick.features.jobseeker.profile.data.datasource

import android.util.Log
import com.example.jobflick.core.network.AuthResponse
import com.example.jobflick.core.network.PocketBaseHttp
import com.example.jobflick.core.network.Record
import com.example.jobflick.features.jobseeker.profile.domain.model.Job
import com.example.jobflick.features.jobseeker.profile.domain.model.JobCategory
import com.example.jobflick.features.jobseeker.profile.domain.model.Profile
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class ProfileRemoteDataSource(
    private val pb: PocketBaseHttp,
    private val profilesCollection: String,
    private val jobsCollection: String
) {

    suspend fun getProfile(): Profile {
        val authResponse = pb.authRefresh(collection = profilesCollection)
        val record = authResponse.record
        return Profile(
            id = record.id,
            name = record.data["name"]?.jsonPrimitive?.content ?: "Unknown",
            photoUrl = record.data["photoUrl"]?.jsonPrimitive?.content
        )
    }

    suspend fun getJobs(): List<Job> {
        val authResponse = pb.authRefresh(collection = profilesCollection)
        val userId = authResponse.record.id

        val jobCategoriesCollection = "jobCategories" // Define the jobCategories collection name

        // Fetch jobCategories collection
        val jobCategoriesResponse = pb.getList(jobCategoriesCollection)
        val jobCategoriesMap = jobCategoriesResponse.items.filter {
            it.data["user"]?.jsonPrimitive?.content == userId // Match user ID
        }.associateBy(
            { it.data["job"]?.jsonPrimitive?.content }, // Map key: job ID
            { it.data["category"]?.jsonPrimitive?.content } // Map value: category
        )

        // Fetch jobs collection
        val response = pb.getList(jobsCollection)

        return response.items.mapNotNull { record ->
            val jobId = record.id
            val category = jobCategoriesMap[jobId] ?: return@mapNotNull null // Remove from list if no match

            Job(
                id = jobId,
                companyName = record.data["companyName"]?.jsonPrimitive?.content ?: "Unknown",
                companyLogoUrl = record.data["companyLogoUrl"]?.jsonPrimitive?.content,
                jobTitle = record.data["jobTitle"]?.jsonPrimitive?.content ?: "Unknown",
                location = record.data["location"]?.jsonPrimitive?.content ?: "Unknown",
                postedTimestamp = record.data["postedTimestamp"]?.jsonPrimitive?.content ?: "",
                level = record.data["level"]?.jsonPrimitive?.content ?: "Unknown",
                salaryRange = record.data["salaryRange"]?.jsonPrimitive?.content ?: "Unknown",
                workType = record.data["workType"]?.jsonPrimitive?.content ?: "Unknown",
                skills = record.data["skills"]?.jsonPrimitive?.content?.split(";")?.map { it.trim() } ?: emptyList(),
                category = JobCategory.valueOf(category),
                statusTimestamp = record.data["statusTimestamp"]?.jsonPrimitive?.content ?: "",
                description = record.data["description"]?.jsonPrimitive?.content ?: "",
                minimumQualifications = record.data["minimumQualifications"]?.jsonPrimitive?.content?.split(";")?.map { it.trim() } ?: emptyList(),
                aboutCompany = record.data["aboutCompany"]?.jsonPrimitive?.content ?: ""
            )
        }
    }

    suspend fun getJobDetail(id: String): Job? {
        val response = pb.getOne(jobsCollection, id)

        return Job(
            id = response.id,
            companyName = response.data["companyName"]?.jsonPrimitive?.content ?: "Unknown",
            companyLogoUrl = response.data["companyLogoUrl"]?.jsonPrimitive?.content,
            jobTitle = response.data["jobTitle"]?.jsonPrimitive?.content ?: "Unknown",
            location = response.data["location"]?.jsonPrimitive?.content ?: "Unknown",
            postedTimestamp = response.data["postedTimestamp"]?.jsonPrimitive?.content ?: "",
            level = response.data["level"]?.jsonPrimitive?.content ?: "Unknown",
            salaryRange = response.data["salaryRange"]?.jsonPrimitive?.content ?: "Unknown",
            workType = response.data["workType"]?.jsonPrimitive?.content ?: "Unknown",
            skills = response.data["skills"]?.jsonPrimitive?.content?.split(";")?.map { it.trim() } ?: emptyList(),
            category = JobCategory.valueOf(response.data["category"]?.jsonPrimitive?.content ?: "OTHER"),
            statusTimestamp = response.data["statusTimestamp"]?.jsonPrimitive?.content ?: "",
            description = response.data["description"]?.jsonPrimitive?.content ?: "",
            minimumQualifications = response.data["minimumQualifications"]?.jsonPrimitive?.content?.split(";")?.map { it.trim() } ?: emptyList(),
            aboutCompany = response.data["aboutCompany"]?.jsonPrimitive?.content ?: ""
        )
    }
}
