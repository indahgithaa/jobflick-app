package com.example.jobflick.features.jobseeker.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobflick.features.jobseeker.profile.domain.model.Job
import com.example.jobflick.features.jobseeker.profile.domain.model.JobCategory
import com.example.jobflick.features.jobseeker.profile.domain.model.Profile
import com.example.jobflick.features.jobseeker.profile.domain.usecases.GetJobDetailUseCase
import com.example.jobflick.features.jobseeker.profile.domain.usecases.GetJobsUseCase
import com.example.jobflick.features.jobseeker.profile.domain.usecases.GetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = true,
    val profile: Profile? = null,
    val selectedTab: JobCategory = JobCategory.MATCH,
    val matchJobs: List<Job> = emptyList(),
    val savedJobs: List<Job> = emptyList(),
    val appliedJobs: List<Job> = emptyList(),
    val errorMessage: String? = null
)

class ProfileViewModel(
    private val getProfile: GetProfileUseCase,
    private val getJobs: GetJobsUseCase,
    private val getJobDetail: GetJobDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadProfileAndJobs()
    }

    fun onTabSelected(category: JobCategory) {
        _uiState.update { it.copy(selectedTab = category) }
    }

    fun refresh() {
        loadProfileAndJobs()
    }

    private fun loadProfileAndJobs() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val profile = getProfile()
                val match = getJobs(JobCategory.MATCH)
                val saved = getJobs(JobCategory.SAVED)
                val applied = getJobs(JobCategory.APPLIED)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        profile = profile,
                        matchJobs = match,
                        savedJobs = saved,
                        appliedJobs = applied
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Terjadi kesalahan"
                    )
                }
            }
        }
    }

    suspend fun loadJobDetail(id: String): Job? =
        getJobDetail(id)
}
