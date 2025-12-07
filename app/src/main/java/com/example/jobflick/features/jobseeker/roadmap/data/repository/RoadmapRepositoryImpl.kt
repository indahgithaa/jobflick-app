package com.example.jobflick.features.jobseeker.roadmap.data.repository

import com.example.jobflick.features.jobseeker.roadmap.data.datasource.RoadmapRemoteDataSource
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapRole
import com.example.jobflick.features.jobseeker.roadmap.domain.repository.RoadmapRepository

class RoadmapRepositoryImpl(
    private val remoteDataSource: RoadmapRemoteDataSource
) : RoadmapRepository {

    override suspend fun getAvailableRoles(): List<String> {
        return remoteDataSource.getAvailableRoles()
    }

    override suspend fun getRoadmapRole(roleName: String): RoadmapRole {
        return remoteDataSource.getRoadmapRole(roleName)
    }

    override suspend fun calculateQuizScore(
        roleName: String,
        moduleId: String,
        answers: List<Int>
    ): Int {
        return remoteDataSource.calculateQuizScore(roleName, moduleId, answers)
    }

    override suspend fun markArticleAsRead(articleId: String) {
        remoteDataSource.markArticleAsRead(articleId)
    }
}
