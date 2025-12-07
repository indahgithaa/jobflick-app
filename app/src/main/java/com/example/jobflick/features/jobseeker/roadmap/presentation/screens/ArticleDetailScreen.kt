package com.example.jobflick.features.jobseeker.roadmap.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapModule
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapRole
import com.example.jobflick.features.jobseeker.roadmap.presentation.components.RoadmapModuleItem
import com.example.jobflick.features.jobseeker.roadmap.presentation.components.RoadmapTopBar

@Composable
fun ArticleDetailScreen(
    role: RoadmapRole,
    module: RoadmapModule,
    articleTitle: String,
    articleIndex: Int,
    onBack: () -> Unit,
    onOpenArticle: (Int) -> Unit,
    onOpenQuiz: () -> Unit,
    onArticleRead: (String) -> Unit
) {
    val currentArticle = module.articles.getOrNull(articleIndex)
        ?: module.articles.firstOrNull()

    LaunchedEffect(currentArticle?.id) {
        currentArticle?.id?.let { onArticleRead(it) }
    }

    Scaffold(
        topBar = {
            RoadmapTopBar(
                title = "Artikel",
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
            Spacer(Modifier.height(16.dp))

            Text(
                text = currentArticle?.title ?: articleTitle,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = currentArticle?.content ?: "",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(32.dp))

            // ==== Next Article / Quiz Section ====
            val nextIndex = articleIndex + 1
            val nextArticle = module.articles.getOrNull(nextIndex)

            if (nextArticle != null) {
                SectionTitle("Selanjutnya")
                Spacer(Modifier.height(8.dp))
                RoadmapModuleItem(
                    title = nextArticle.title,
                    onClick = { onOpenArticle(nextIndex) }
                )
            } else {
                // Kalau tidak ada artikel berikutnya, tawarkan langsung kuis
                SectionTitle("Kuis")
                Spacer(Modifier.height(8.dp))
                RoadmapModuleItem(
                    title = module.quizTitle,
                    onClick = onOpenQuiz
                )
            }

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
