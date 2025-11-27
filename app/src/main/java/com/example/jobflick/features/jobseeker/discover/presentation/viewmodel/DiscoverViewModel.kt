package com.example.jobflick.features.jobseeker.discover.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobflick.features.jobseeker.discover.domain.model.JobPosting
import com.example.jobflick.features.jobseeker.discover.domain.usecase.GetDiscoverJobsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DiscoverUiState(
    val isLoading: Boolean = false,
    val jobs: List<JobPosting> = emptyList(),
    val currentIndex: Int = 0,
    val savedJobs: List<JobPosting> = emptyList(),
    val appliedJobs: List<JobPosting> = emptyList(),
    val skippedJobs: List<JobPosting> = emptyList(),
    val errorMessage: String? = null
) {
    val currentJob: JobPosting?
        get() = jobs.getOrNull(currentIndex)

    val hasCards: Boolean
        get() = jobs.isNotEmpty() && currentIndex < jobs.size
}

private data class DiscoverHistoryEntry(
    val uiState: DiscoverUiState
)

class DiscoverViewModel(
    private val getDiscoverJobsUseCase: GetDiscoverJobsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState(isLoading = true))
    val uiState: StateFlow<DiscoverUiState> = _uiState

    private val history = ArrayDeque<DiscoverHistoryEntry>()

    init {
        loadJobs()
    }

    private fun loadJobs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val jobs = getDiscoverJobsUseCase()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        jobs = jobs,
                        currentIndex = 0
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "Gagal memuat data")
                }
            }
        }
    }

    private fun pushHistory() {
        history.addLast(DiscoverHistoryEntry(_uiState.value))
    }

    fun undoLastAction() {
        val last = history.removeLastOrNull() ?: return
        _uiState.value = last.uiState
    }

    private fun moveToNextCard() {
        _uiState.update { state ->
            val next = (state.currentIndex + 1).coerceAtMost(state.jobs.size)
            state.copy(currentIndex = next)
        }
    }

    fun skipCurrentJob() {
        val job = _uiState.value.currentJob ?: return
        pushHistory()
        _uiState.update { state ->
            state.copy(skippedJobs = state.skippedJobs + job)
        }
        moveToNextCard()
    }

    fun applyCurrentJob() {
        val job = _uiState.value.currentJob ?: return
        pushHistory()
        _uiState.update { state ->
            state.copy(appliedJobs = state.appliedJobs + job)
        }
        moveToNextCard()
    }

    fun saveCurrentJob() {
        val job = _uiState.value.currentJob ?: return
        pushHistory()
        _uiState.update { state ->
            if (state.savedJobs.any { it.id == job.id }) state
            else state.copy(savedJobs = state.savedJobs + job)
        }
    }
}
