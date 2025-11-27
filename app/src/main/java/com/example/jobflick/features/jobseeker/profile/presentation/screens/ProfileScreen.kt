package com.example.jobflick.features.jobseeker.profile.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.jobflick.features.jobseeker.profile.domain.model.Job
import com.example.jobflick.features.jobseeker.profile.domain.model.JobCategory
import com.example.jobflick.features.jobseeker.profile.domain.model.Profile
import com.example.jobflick.features.jobseeker.profile.presentation.components.JobItemCard
import com.example.jobflick.features.jobseeker.profile.presentation.components.JobStatusTabRow
import com.example.jobflick.features.jobseeker.profile.presentation.components.ProfileHeader

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
    profile: Profile,
    selectedTab: JobCategory,
    matchJobs: List<Job>,
    savedJobs: List<Job>,
    appliedJobs: List<Job>,
    onTabSelected: (JobCategory) -> Unit,
    onJobClick: (Job) -> Unit,
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

        ProfileTopBar(
            onSettingsClick = onSettingsClick,
            onLogoutClick = onLogoutClick
        )

        Spacer(Modifier.height(16.dp))

        ProfileHeader(
            profile = profile,
            onClick = onSeeProfileClick
        )

        Spacer(Modifier.height(24.dp))

        JobStatusTabRow(
            selected = selectedTab,
            onSelected = onTabSelected,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        val jobs = when (selectedTab) {
            JobCategory.MATCH -> matchJobs
            JobCategory.SAVED -> savedJobs
            JobCategory.APPLIED -> appliedJobs
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(jobs, key = { it.id }) { job ->
                JobItemCard(
                    job = job,
                    onClick = { onJobClick(job) }
                )
            }
        }
    }
}

@Composable
private fun ProfileTopBar(
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
