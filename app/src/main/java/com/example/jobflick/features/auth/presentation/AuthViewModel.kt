package com.example.jobflick.features.auth.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobflick.core.global.AppGraph
import kotlinx.coroutines.launch

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val repository = AppGraph.authRepository

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    fun signUp(email: String, password: String, name: String, role: String) = launch {
        repository.signUp(email, password, name, role)
            .onSuccess { authState = AuthState.Success }
            .onFailure { authState = AuthState.Error(it.message ?: "Sign up failed") }
    }

    fun signIn(email: String, password: String) = launch {
        repository.signIn(email, password)
            .onSuccess { authState = AuthState.Success }
            .onFailure { authState = AuthState.Error(it.message ?: "Sign in failed") }
    }

    fun signOut() { repository.signOut(); authState = AuthState.Idle }

    private fun launch(block: suspend () -> Unit) {
        authState = AuthState.Loading
        viewModelScope.launch { block() }
    }
}
