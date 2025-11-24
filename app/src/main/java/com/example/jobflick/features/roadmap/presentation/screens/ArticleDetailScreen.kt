package com.example.jobflick.features.roadmap.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jobflick.R
import com.example.jobflick.features.roadmap.domain.model.RoadmapModule
import com.example.jobflick.features.roadmap.domain.model.RoadmapRole
import com.example.jobflick.features.roadmap.presentation.components.RoadmapTopBar

@Composable
fun ArticleDetailScreen(
    role: RoadmapRole,
    module: RoadmapModule,
    articleTitle: String,
    articleIndex: Int,
    onBack: () -> Unit,
    onOpenArticle: (Int) -> Unit,
    onOpenQuiz: () -> Unit
) {
    val isLastArticle = articleIndex == module.articles.lastIndex

    val nextLabel = if (!isLastArticle) {
        "Selanjutnya: ${module.articles[articleIndex + 1]}"
    } else {
        "Selanjutnya: ${module.quizTitle}"
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
            Spacer(Modifier.height(12.dp))

            Text(
                text = articleTitle,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(16.dp))

            // sementara: isi artikel = deskripsi modul
            Text(
                text = module.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(32.dp))

            val grayText = Color(0xFF808080)
            val grayBorder = Color(0xFFDDDDDD)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, grayBorder, RoundedCornerShape(20.dp))
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .clickable {
                        if (!isLastArticle) {
                            onOpenArticle(articleIndex + 1)
                        } else {
                            onOpenQuiz()
                        }
                    }
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = nextLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        color = grayText,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Image(
                        painter = painterResource(id = R.drawable.next),
                        contentDescription = "Lanjut",
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
