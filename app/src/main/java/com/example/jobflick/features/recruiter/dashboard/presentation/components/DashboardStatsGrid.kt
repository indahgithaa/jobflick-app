package com.example.jobflick.features.recruiter.dashboard.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobflick.core.ui.theme.Jost
import com.example.jobflick.core.ui.theme.OrangePrimary

@Composable
fun DashboardStatsGrid(
    totalJobs: Int,
    totalApplicants: Int,
    totalInterviews: Int,     // nggak dipakai di UI lagi, tapi biarkan di param biar kompatibel
    totalAccepted: Int,       // idem
    lastUpdatedText: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        DashboardStatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.Work,
            title = "Total Lowongan",
            value = totalJobs.toString(),
            lastUpdatedText = lastUpdatedText
        )
        Spacer(modifier = Modifier.width(16.dp))
        DashboardStatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.People,
            title = "Total Pelamar",
            value = totalApplicants.toString(),
            lastUpdatedText = lastUpdatedText
        )
    }
}

@Composable
private fun DashboardStatCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    lastUpdatedText: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(OrangePrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    fontFamily = Jost,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = value,
                fontFamily = Jost,
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Diperbarui: $lastUpdatedText",
                fontSize = 12.sp,
                color = Color(0xFF9E9E9E)
            )
        }
    }
}
