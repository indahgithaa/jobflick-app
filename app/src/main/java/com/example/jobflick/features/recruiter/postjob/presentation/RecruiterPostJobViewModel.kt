package com.example.jobflick.features.recruiter.postjob.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobflick.features.recruiter.postjob.domain.model.RecruiterJobPostRequest
import com.example.jobflick.features.recruiter.postjob.domain.repository.RecruiterJobPostRepository
import kotlinx.coroutines.launch

data class RecruiterPostJobUiState(
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null
)

class RecruiterPostJobViewModel(
    private val repository: RecruiterJobPostRepository
) : ViewModel() {

    val uiState = mutableStateOf(RecruiterPostJobUiState())

    fun submitJob(
        request: RecruiterJobPostRequest,
        onSuccess: () -> Unit
    ) {
        uiState.value = uiState.value.copy(isSubmitting = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val success = repository.createJob(request)
                if (success) {
                    onSuccess()
                } else {
                    uiState.value = uiState.value.copy(
                        isSubmitting = false,
                        errorMessage = "Gagal mengunggah lowongan"
                    )
                }
            } catch (e: Exception) {
                uiState.value = uiState.value.copy(
                    isSubmitting = false,
                    errorMessage = e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }
}
