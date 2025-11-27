package com.example.jobflick.features.jobseeker.roadmap.domain.repository

import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapRole

interface RoadmapRepository {
    fun getRoadmapRole(roleName: String): RoadmapRole
}
