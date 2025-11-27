package com.example.jobflick.features.jobseeker.roadmap.domain.usecases

import com.example.jobflick.features.jobseeker.roadmap.domain.repository.RoadmapRepository

class GetAvailableRolesUseCase(
    private val repository: RoadmapRepository
) {
    operator fun invoke(): List<String> = repository.getAvailableRoles()
}
