package com.example.jobflick.features.jobseeker.roadmap.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapModule
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapRole
import com.example.jobflick.features.jobseeker.roadmap.domain.usecases.CalculateQuizScoreUseCase
import com.example.jobflick.features.jobseeker.roadmap.domain.usecases.GetAvailableRolesUseCase
import com.example.jobflick.features.jobseeker.roadmap.domain.usecases.GetRoadmapRoleUseCase

class RoadmapViewModel(
    private val getAvailableRolesUseCase: GetAvailableRolesUseCase,
    private val getRoadmapRoleUseCase: GetRoadmapRoleUseCase,
    private val calculateQuizScoreUseCase: CalculateQuizScoreUseCase
) : ViewModel() {

    var availableRoles by mutableStateOf<List<String>>(emptyList())
        private set

    var selectedRole by mutableStateOf<RoadmapRole?>(null)
        private set

    var selectedModule by mutableStateOf<RoadmapModule?>(null)
        private set

    init {
        availableRoles = getAvailableRolesUseCase()
    }

    fun selectRole(roleName: String) {
        selectedRole = getRoadmapRoleUseCase(roleName)
        selectedModule = null
    }

    fun selectModule(module: RoadmapModule) {
        selectedModule = module
    }

    /**
     * Dipanggil ketika user menyelesaikan kuis.
     * answers: index jawaban per soal (0-based).
     */
    fun finishQuiz(answers: List<Int>): Int {
        val role = selectedRole ?: return 0
        val module = selectedModule ?: return 0
        val score = calculateQuizScoreUseCase(role.name, module.id, answers)

        // refresh data role setelah backend dummy mengubah completion
        selectedRole = getRoadmapRoleUseCase(role.name)

        // update selectedModule ke versi terbaru (status & progress baru)
        selectedModule =
            selectedRole?.modules?.firstOrNull { it.id == module.id } ?: selectedModule

        return score
    }
}
