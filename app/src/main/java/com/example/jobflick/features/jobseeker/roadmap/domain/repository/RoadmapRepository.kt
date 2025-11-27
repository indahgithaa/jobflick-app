package com.example.jobflick.features.jobseeker.roadmap.domain.repository

import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapRole

interface RoadmapRepository {

    /** List nama role yang tersedia (untuk layar pemilihan role) */
    fun getAvailableRoles(): List<String>

    /** Detail roadmap untuk role tertentu */
    fun getRoadmapRole(roleName: String): RoadmapRole

    /**
     * Hitung skor kuis dan update status modul di backend (dummy).
     *
     * @param roleName nama role, harus sama dengan yang digunakan di getRoadmapRole
     * @param moduleId id modul (mis. "senior_se_mod1")
     * @param answers  list index jawaban user per soal (0-based)
     * @return score 0..100
     */
    fun calculateQuizScore(
        roleName: String,
        moduleId: String,
        answers: List<Int>
    ): Int
}
