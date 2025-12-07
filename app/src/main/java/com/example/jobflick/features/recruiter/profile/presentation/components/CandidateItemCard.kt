package com.example.jobflick.features.recruiter.profile.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.jobflick.core.ui.theme.BluePrimary
import com.example.jobflick.core.ui.theme.OrangePrimary
import com.example.jobflick.features.recruiter.profile.domain.model.Candidate
import com.example.jobflick.features.recruiter.profile.domain.model.CandidatePipelineStatus

@Composable
fun CandidateItemCard(
    candidate: Candidate,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = Color.White,
        shadowElevation = 0.dp,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // AVATAR
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE5F3FF)),
                contentAlignment = Alignment.Center
            ) {
                if (candidate.avatarUrl != null) {
                    AsyncImage(
                        model = candidate.avatarUrl,
                        contentDescription = candidate.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        imageVector = Icons.Default.Person,
                        contentDescription = candidate.name,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = candidate.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF202124)
                    )
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = candidate.positionTitle,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF7A7A7A)
                    )
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                horizontalAlignment = Alignment.End
            ) {
                CandidateStatusChip(status = candidate.pipelineStatus)

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Lihat detail",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = OrangePrimary,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Composable
private fun CandidateStatusChip(
    status: CandidatePipelineStatus
) {
    val label = when (status) {
        CandidatePipelineStatus.DIPROSES -> "Diproses"
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(BluePrimary)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
