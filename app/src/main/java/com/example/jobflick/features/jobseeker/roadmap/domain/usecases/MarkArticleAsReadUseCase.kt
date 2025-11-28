package com.example.jobflick.features.jobseeker.roadmap.domain.usecases

import com.example.jobflick.features.jobseeker.roadmap.domain.repository.RoadmapRepository

class MarkArticleAsReadUseCase(
    private val repository: RoadmapRepository
) {
    suspend operator fun invoke(articleId: String) =
        repository.markArticleAsRead(articleId)
}
