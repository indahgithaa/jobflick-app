package com.example.jobflick.features.jobseeker.roadmap.domain.usecases

import com.example.jobflick.features.jobseeker.roadmap.domain.repository.RoadmapRepository

class CalculateQuizScoreUseCase(
    private val repository: RoadmapRepository
) {
    suspend operator fun invoke(
        roleName: String,
        moduleId: String,
        answers: List<Int>
    ): Int = repository.calculateQuizScore(roleName, moduleId, answers)
}
