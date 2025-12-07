package com.example.jobflick.features.jobseeker.roadmap.domain.usecases

import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapRole
import com.example.jobflick.features.jobseeker.roadmap.domain.repository.RoadmapRepository

class GetRoadmapRoleUseCase(
    private val repository: RoadmapRepository
) {
    suspend operator fun invoke(roleName: String): RoadmapRole =
        repository.getRoadmapRole(roleName)
}
