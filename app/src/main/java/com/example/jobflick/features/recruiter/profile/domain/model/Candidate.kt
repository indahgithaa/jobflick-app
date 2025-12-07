package com.example.jobflick.features.recruiter.profile.domain.model

data class Candidate(
    val id: String,
    val name: String,
    val positionTitle: String,
    val avatarUrl: String? = null,
    val pipelineStatus: CandidatePipelineStatus = CandidatePipelineStatus.DIPROSES
)

enum class CandidatePipelineStatus {
    DIPROSES
    // nanti bisa ditambah: LOLOS, DITOLAK, DLL
}
