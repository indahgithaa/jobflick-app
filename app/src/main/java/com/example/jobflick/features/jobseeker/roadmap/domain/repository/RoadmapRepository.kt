package com.example.jobflick.features.jobseeker.roadmap.domain.repository

import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapRole

interface RoadmapRepository {

    /** List nama role yang tersedia (untuk layar pemilihan role) */
    suspend fun getAvailableRoles(): List<String>

    /** Detail roadmap untuk role tertentu */
    suspend fun getRoadmapRole(roleName: String): RoadmapRole

    /**
     * Hitung skor kuis dan update status modul di backend (dummy).
     *
     * @param roleName nama role, harus sama dengan yang digunakan di getRoadmapRole
     * @param moduleId id modul (mis. "senior_se_mod1")
     * @param answers  list index jawaban user per soal (0-based)
     * @return score 0..100
     */
    suspend fun calculateQuizScore(
        roleName: String,
        moduleId: String,
        answers: List<Int>
    ): Int

    /**
     * Mark an article as read and update progress.
     *
     * @param articleId id of the article
     */
    suspend fun markArticleAsRead(articleId: String)
}
