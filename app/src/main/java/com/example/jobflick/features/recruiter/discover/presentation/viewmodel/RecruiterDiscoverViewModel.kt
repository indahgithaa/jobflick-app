package com.example.jobflick.features.recruiter.discover.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobflick.features.recruiter.discover.domain.model.CandidateProfile
import com.example.jobflick.features.recruiter.discover.domain.usecase.GetRecruiterCandidatesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RecruiterDiscoverUiState(
    val isLoading: Boolean = false,
    val currentRole: String = "Junior Software Engineer",
    val candidates: List<CandidateProfile> = emptyList(),
    val currentIndex: Int = 0,
    val savedCandidates: List<CandidateProfile> = emptyList(),
    val contactedCandidates: List<CandidateProfile> = emptyList(),
    val skippedCandidates: List<CandidateProfile> = emptyList(),
    val errorMessage: String? = null
) {
    val currentCandidate: CandidateProfile?
        get() = candidates.getOrNull(currentIndex)

    val hasCards: Boolean
        get() = candidates.isNotEmpty() && currentIndex < candidates.size
}

private data class HistoryEntry(val state: RecruiterDiscoverUiState)

class RecruiterDiscoverViewModel(
    private val getCandidatesUseCase: GetRecruiterCandidatesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecruiterDiscoverUiState(isLoading = true))
    val uiState: StateFlow<RecruiterDiscoverUiState> = _uiState

    private val history = ArrayDeque<HistoryEntry>()

    init {
        loadCandidates()
    }

    fun setRole(roleName: String) {
        _uiState.update { it.copy(currentRole = roleName) }
        loadCandidates()
    }

    private fun loadCandidates() {
        viewModelScope.launch {
            val roleName = _uiState.value.currentRole
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val data = getCandidatesUseCase(roleName)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        candidates = data,
                        currentIndex = 0,
                        skippedCandidates = emptyList(),
                        savedCandidates = emptyList(),
                        contactedCandidates = emptyList()
                    )
                }
                history.clear()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "Gagal memuat kandidat")
                }
            }
        }
    }

    private fun pushHistory() {
        history.addLast(HistoryEntry(_uiState.value))
    }

    fun undoLast() {
        val last = history.removeLastOrNull() ?: return
        _uiState.value = last.state
    }

    private fun moveNextCard() {
        _uiState.update { state ->
            val next = (state.currentIndex + 1).coerceAtMost(state.candidates.size)
            state.copy(currentIndex = next)
        }
    }

    fun skipCurrent() {
        val candidate = _uiState.value.currentCandidate ?: return
        pushHistory()
        _uiState.update { state ->
            state.copy(skippedCandidates = state.skippedCandidates + candidate)
        }
        moveNextCard()
    }

    fun contactCurrent() {
        val candidate = _uiState.value.currentCandidate ?: return
        pushHistory()
        _uiState.update { state ->
            state.copy(contactedCandidates = state.contactedCandidates + candidate)
        }
        moveNextCard()
    }

    fun saveCurrent() {
        val candidate = _uiState.value.currentCandidate ?: return
        pushHistory()
        _uiState.update { state ->
            if (state.savedCandidates.any { it.id == candidate.id }) state
            else state.copy(savedCandidates = state.savedCandidates + candidate)
        }
    }
}
