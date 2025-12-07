package com.example.jobflick.features.recruiter.profile.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.jobflick.core.ui.components.RecruiterBottomNavBar
import com.example.jobflick.features.recruiter.profile.domain.model.*
import com.example.jobflick.features.recruiter.profile.presentation.screens.RecruiterProfileScreen

@Composable
fun RecruiterProfileRoute(
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    onLogoutToSignIn: () -> Unit
) {
    // ========= sementara: dummy data, nanti diganti ViewModel =========
    var selectedTab by remember { mutableStateOf(CandidateCategory.MATCH) }

    val profile = remember {
        RecruiterProfile(
            id = "recruiter_01",
            name = "Riko Prasetya",
            photoUrl = null
        )
    }

    val matchCandidates = remember {
        listOf(
            Candidate(
                id = "c1",
                name = "Jessica Shannon",
                positionTitle = "Data Engineer",
                avatarUrl = null
            )
        )
    }
    val savedCandidates = remember { emptyList<Candidate>() }
    val contactedCandidates = remember { emptyList<Candidate>() }

    Scaffold(
        bottomBar = {
            RecruiterBottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = onTabSelected
            )
        }
    ) { innerPadding ->
        RecruiterProfileScreen(
            profile = profile,
            selectedTab = selectedTab,
            matchCandidates = matchCandidates,
            savedCandidates = savedCandidates,
            contactedCandidates = contactedCandidates,
            onTabSelected = { selectedTab = it },
            onCandidateClick = { /* TODO: detail kandidat */ },
            onSeeProfileClick = { /* TODO: edit profil recruiter */ },
            onSettingsClick = { /* TODO: buka settings recruiter */ },
            onLogoutClick = onLogoutToSignIn,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
