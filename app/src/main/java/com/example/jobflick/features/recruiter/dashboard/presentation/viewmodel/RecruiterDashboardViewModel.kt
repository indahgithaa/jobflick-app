package com.example.jobflick.features.recruiter.dashboard.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobflick.features.recruiter.dashboard.domain.repository.RecruiterDashboardRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RecruiterDashboardViewModel(
    private val repository: RecruiterDashboardRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(RecruiterDashboardUiState())
    val uiState: State<RecruiterDashboardUiState> = _uiState

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                // parallel fetch jobs + stats
                val jobsDeferred = async { repository.getActiveJobs() }
                val statsDeferred = async { repository.getDashboardStats() }

                val jobs = jobsDeferred.await()
                val stats = statsDeferred.await()

                _uiState.value = RecruiterDashboardUiState(
                    isLoading = false,
                    activeJobs = jobs,
                    stats = stats,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Terjadi kesalahan tak terduga"
                )
            }
        }
    }
}
