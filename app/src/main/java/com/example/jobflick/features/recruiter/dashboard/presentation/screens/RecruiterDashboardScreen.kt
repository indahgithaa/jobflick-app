package com.example.jobflick.features.recruiter.dashboard.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobflick.core.common.toTimeAgoLabel
import com.example.jobflick.core.ui.components.RecruiterBottomNavBar
import com.example.jobflick.core.ui.theme.Jost
import com.example.jobflick.core.ui.theme.OrangePrimary
import com.example.jobflick.features.recruiter.dashboard.domain.model.RecruiterJobSummary
import com.example.jobflick.features.recruiter.dashboard.presentation.RecruiterDashboardUiState
import com.example.jobflick.navigation.Routes

@Composable
fun RecruiterDashboardScreen(
    currentRoute: String,
    uiState: RecruiterDashboardUiState,
    onTabSelected: (String) -> Unit,
    onAddJobClick: () -> Unit,
    onJobClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            RecruiterBottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = onTabSelected
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dashboard",
                        fontFamily = Jost,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 32.sp
                    )
                    Icon(
                        imageVector = Icons.Filled.FilterList,
                        contentDescription = "Filter",
                        tint = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    uiState.errorMessage != null -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = uiState.errorMessage,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Red
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Coba lagi",
                                color = OrangePrimary,
                                modifier = Modifier.clickable { onRetry() }
                            )
                        }
                    }

                    else -> {
                        uiState.stats?.let { stats ->
                            ActiveJobsCard(
                                jobs = uiState.activeJobs,
                                onAddJobClick = onAddJobClick,
                                onJobClick = onJobClick
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            DashboardStatsGrid(
                                totalJobs = stats.totalJobs,
                                totalApplicants = stats.totalApplicants,
                                totalInterviews = stats.totalInterviews,
                                totalAccepted = stats.totalAccepted,
                                lastUpdatedText = stats.lastUpdatedText
                            )
                        }
                    }
                }
            }
        }
    }
}

/* ===================== ACTIVE JOBS CARD ===================== */

@Composable
private fun ActiveJobsCard(
    jobs: List<RecruiterJobSummary>,
    onAddJobClick: () -> Unit,
    onJobClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lowongan Aktif",
                    fontFamily = Jost,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(OrangePrimary)
                        .clickable { onAddJobClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Tambah Lowongan",
                        tint = Color.White
                    )
                }
            }

            Divider(color = Color(0xFFE6E6E6))

            Column {
                jobs.forEachIndexed { index, job ->
                    ActiveJobItem(
                        job = job,
                        onClick = { onJobClick(job.id) }
                    )

                    if (index != jobs.lastIndex) {
                        Divider(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            color = Color(0xFFE6E6E6)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActiveJobItem(
    job: RecruiterJobSummary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = job.title,
                fontFamily = Jost,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF9E9E9E)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = job.city,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9E9E9E)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF9E9E9E)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = job.createdAt.toTimeAgoLabel(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9E9E9E)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = job.applicantsCount.toString(),
                fontFamily = Jost,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Text(
                text = "Pelamar",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9E9E9E)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = job.viewsCount.toString(),
                fontFamily = Jost,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Text(
                text = "Lihat",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9E9E9E)
            )
        }
    }
}
