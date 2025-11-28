package com.example.jobflick.features.jobseeker.roadmap.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapModule
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapModuleStatus
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapRole
import com.example.jobflick.features.jobseeker.roadmap.domain.usecases.CalculateQuizScoreUseCase
import com.example.jobflick.features.jobseeker.roadmap.domain.usecases.GetAvailableRolesUseCase
import com.example.jobflick.features.jobseeker.roadmap.domain.usecases.GetRoadmapRoleUseCase
import com.example.jobflick.features.jobseeker.roadmap.domain.usecases.MarkArticleAsReadUseCase
import kotlinx.coroutines.launch

class RoadmapViewModel(
    private val getAvailableRolesUseCase: GetAvailableRolesUseCase,
    private val getRoadmapRoleUseCase: GetRoadmapRoleUseCase,
    private val calculateQuizScoreUseCase: CalculateQuizScoreUseCase,
    private val markArticleAsReadUseCase: MarkArticleAsReadUseCase
) : ViewModel() {

    var availableRoles by mutableStateOf<List<String>>(emptyList())
        private set

    private var cachedRoles by mutableStateOf<Map<String, RoadmapRole>>(emptyMap())

    var selectedRole by mutableStateOf<RoadmapRole?>(null)
        private set

    var selectedModule by mutableStateOf<RoadmapModule?>(null)
        private set

    init {
        viewModelScope.launch {
            availableRoles = getAvailableRolesUseCase()
        }
    }

    fun selectRole(roleName: String) {
        viewModelScope.launch {
            val cached = cachedRoles[roleName]
            if (cached != null) {
                selectedRole = cached
            } else {
                val fetched = getRoadmapRoleUseCase(roleName)
                cachedRoles = cachedRoles + (roleName to fetched)
                selectedRole = fetched
            }
            selectedModule = null
        }
    }

    fun selectModule(module: RoadmapModule) {
        selectedModule = module
    }

    /**
     * Dipanggil ketika user menyelesaikan kuis.
     * answers: index jawaban per soal (0-based).
     */
    suspend fun finishQuiz(answers: List<Int>): Int {
        val role = selectedRole ?: return 0
        val module = selectedModule ?: return 0
        val score = calculateQuizScoreUseCase(role.name, module.id, answers)

        // Refetch the roadmap to update progress
        val updatedRole = getRoadmapRoleUseCase(role.name)
        cachedRoles = cachedRoles + (role.name to updatedRole)
        selectedRole = updatedRole
        selectedModule = updatedRole.modules.firstOrNull { it.id == module.id }

        return score
    }

    /**
     * Mark an article as read and refetch progress.
     */
    suspend fun markArticleAsRead(articleId: String) {
        markArticleAsReadUseCase(articleId)
        // Refetch the roadmap to update progress
        val role = selectedRole ?: return
        val updatedRole = getRoadmapRoleUseCase(role.name)
        cachedRoles = cachedRoles + (role.name to updatedRole)
        selectedRole = updatedRole
        selectedModule = updatedRole.modules.firstOrNull { it.id == selectedModule?.id }
    }
}
