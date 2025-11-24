package com.example.jobflick.features.roadmap.domain.repository

import com.example.jobflick.features.roadmap.domain.model.RoadmapRole

interface RoadmapRepository {
    fun getRoadmapRole(roleName: String): RoadmapRole
}
