package com.example.jobflick.features.jobseeker.roadmap.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jobflick.core.ui.components.JFLinearProgress
import com.example.jobflick.core.ui.theme.BluePrimary
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapModule
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapRole
import com.example.jobflick.features.jobseeker.roadmap.presentation.components.RoadmapModuleCard
import com.example.jobflick.features.jobseeker.roadmap.presentation.components.RoadmapModuleStatus
import com.example.jobflick.features.jobseeker.roadmap.presentation.components.RoadmapTopBar
import kotlin.math.roundToInt

@Composable
fun RoadmapOverviewScreen(
    role: RoadmapRole,
    onBack: () -> Unit,
    onSelectModule: (RoadmapModule) -> Unit
) {
    Scaffold(
        topBar = {
            RoadmapTopBar(
                title = "Roadmap",
                subtitle = role.name,
                onBack = onBack
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))

            // Progress bar + persen di kanan
            val progressPercent = (role.progress * 100).roundToInt()
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                JFLinearProgress(
                    progress = role.progress,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Progress: $progressPercent%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BluePrimary
                )
            }

            Spacer(Modifier.height(20.dp))

            role.modules.forEachIndexed { index, module ->
                val status = when (index) {
                    0 -> RoadmapModuleStatus.COMPLETED   // kartu 1 → Selesai (biru)
                    1 -> RoadmapModuleStatus.IN_PROGRESS // kartu 2 → Mulai (oren)
                    else -> RoadmapModuleStatus.LOCKED   // sisanya → Terkunci (abu)
                }

                RoadmapModuleCard(
                    index = index + 1,
                    module = module,
                    status = status,
                    onClick = { onSelectModule(module) }
                )

            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
