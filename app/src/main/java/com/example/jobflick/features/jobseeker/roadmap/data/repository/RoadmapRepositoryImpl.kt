package com.example.jobflick.features.jobseeker.roadmap.data.repository

import com.example.jobflick.features.jobseeker.roadmap.data.remote.RoadmapRemoteDataSource
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapRole
import com.example.jobflick.features.jobseeker.roadmap.domain.repository.RoadmapRepository

class RoadmapRepositoryImpl(
    private val remote: RoadmapRemoteDataSource
) : RoadmapRepository {

    override fun getRoadmapRole(roleName: String): RoadmapRole {
        return remote.getRoadmapRole(roleName)
    }
}
