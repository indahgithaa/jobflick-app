package com.example.jobflick.features.recruiter.profile.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobflick.features.recruiter.profile.domain.model.Candidate
import com.example.jobflick.features.recruiter.profile.domain.model.CandidateCategory
import com.example.jobflick.features.recruiter.profile.domain.model.RecruiterProfile
import com.example.jobflick.features.recruiter.profile.presentation.components.CandidateItemCard
import com.example.jobflick.features.recruiter.profile.presentation.components.CandidateStatusTabRow
import com.example.jobflick.features.recruiter.profile.presentation.components.RecruiterProfileHeader

@Composable
fun RecruiterProfileScreen(
    profile: RecruiterProfile,
    selectedTab: CandidateCategory,
    matchCandidates: List<Candidate>,
    savedCandidates: List<Candidate>,
    contactedCandidates: List<Candidate>,
    onTabSelected: (CandidateCategory) -> Unit,
    onCandidateClick: (Candidate) -> Unit,
    onSeeProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(24.dp))

        RecruiterProfileTopBar(
            onSettingsClick = onSettingsClick,
            onLogoutClick = onLogoutClick
        )

        Spacer(Modifier.height(16.dp))

        RecruiterProfileHeader(
            profile = profile,
            onClick = onSeeProfileClick
        )

        Spacer(Modifier.height(24.dp))

        CandidateStatusTabRow(
            selected = selectedTab,
            onSelected = onTabSelected,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        val candidates = when (selectedTab) {
            CandidateCategory.MATCH -> matchCandidates
            CandidateCategory.SAVED -> savedCandidates
            CandidateCategory.CONTACTED -> contactedCandidates
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(candidates, key = { it.id }) { candidate ->
                CandidateItemCard(
                    candidate = candidate,
                    onClick = { onCandidateClick(candidate) }
                )
            }
        }
    }
}

@Composable
private fun RecruiterProfileTopBar(
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onSettingsClick) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Pengaturan",
                tint = Color.Black
            )
        }

        IconButton(onClick = onLogoutClick) {
            Icon(
                imageVector = Icons.Outlined.ExitToApp,
                contentDescription = "Keluar",
                tint = Color(0xFFDB2C2C)
            )
        }
    }
}
