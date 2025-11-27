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
import com.example.jobflick.core.ui.theme.OrangePrimary
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapModule
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapRole
import com.example.jobflick.features.jobseeker.roadmap.presentation.components.RoadmapModuleItem
import com.example.jobflick.features.jobseeker.roadmap.presentation.components.RoadmapTopBar
import kotlin.math.roundToInt

@Composable
fun ModuleDetailScreen(
    role: RoadmapRole,
    module: RoadmapModule,
    onBack: () -> Unit,
    onOpenArticle: (Int) -> Unit,
    onOpenQuiz: () -> Unit
) {
    Scaffold(
        topBar = {
            RoadmapTopBar(
                title = "Roadmap",
                subtitle = module.title,
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

            Text(
                text = module.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(8.dp))

            val progressPercent = (module.progress * 100).roundToInt()
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                JFLinearProgress(
                    progress = module.progress,
                    progressColor = OrangePrimary,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Progress: $progressPercent%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OrangePrimary
                )
            }

            Spacer(Modifier.height(24.dp))

            SectionTitle("Deskripsi")
            Text(
                text = module.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(20.dp))

            SectionTitle("Yang akan Dipelajari")
            Spacer(Modifier.height(8.dp))
            module.learningPoints.forEach {
                BulletText(it)
            }

            Spacer(Modifier.height(24.dp))

            SectionTitle("Artikel")
            Spacer(Modifier.height(8.dp))
            module.articles.forEachIndexed { index, article ->
                RoadmapModuleItem(
                    title = article.title,
                    onClick = { onOpenArticle(index) }
                )
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(24.dp))

            SectionTitle("Kuis")
            RoadmapModuleItem(
                title = module.quizTitle,
                onClick = onOpenQuiz
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold
        )
    )
}

@Composable
private fun BulletText(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text("•  ")
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
