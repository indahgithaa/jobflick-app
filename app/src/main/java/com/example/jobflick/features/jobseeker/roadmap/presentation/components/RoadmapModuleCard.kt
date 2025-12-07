package com.example.jobflick.features.jobseeker.roadmap.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jobflick.core.ui.theme.BluePrimary
import com.example.jobflick.core.ui.theme.GrayInactive
import com.example.jobflick.core.ui.theme.OrangePrimary
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapModule
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapModuleStatus

@Composable
fun RoadmapModuleCard(
    index: Int,
    module: RoadmapModule,
    status: com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapModuleStatus,
    onClick: () -> Unit
) {
    val isClickable = status != RoadmapModuleStatus.LOCKED

    val (statusText, statusColor, circleColor) = when (status) {
        RoadmapModuleStatus.COMPLETED ->
            Triple("Selesai", BluePrimary, BluePrimary)
        RoadmapModuleStatus.IN_PROGRESS ->
            Triple("Mulai", OrangePrimary, OrangePrimary)
        RoadmapModuleStatus.LOCKED ->
            Triple("Terkunci", GrayInactive, Color(0xFFBDBDBD))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(circleColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = index.toString(),
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .background(Color.White, RoundedCornerShape(20.dp))
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(20.dp))
                .then(
                    if (isClickable) Modifier.clickable(onClick = onClick)
                    else Modifier
                )
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = module.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(Modifier.height(4.dp))
            Text(
                text = module.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                InfoChip("${module.articles.size} Artikel")
                Spacer(Modifier.width(8.dp))
                InfoChip("1 Kuis")

                Spacer(Modifier.weight(1f))

                StatusChip(
                    text = statusText,
                    backgroundColor = statusColor
                )
            }
        }
    }
}

@Composable
private fun InfoChip(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFF1F1F1), RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun StatusChip(
    text: String,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(999.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Color.White
        )
    }
}
