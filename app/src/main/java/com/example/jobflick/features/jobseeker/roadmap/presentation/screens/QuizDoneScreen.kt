package com.example.jobflick.features.jobseeker.roadmap.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobflick.R
import com.example.jobflick.core.ui.components.JFPrimaryButton
import com.example.jobflick.core.ui.theme.BluePrimary
import com.example.jobflick.core.ui.theme.Jost
import com.example.jobflick.features.jobseeker.roadmap.presentation.components.RoadmapTopBar

@Composable
fun QuizDoneScreen(
    score: Int,
    onClose: () -> Unit
) {
    Scaffold(

    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(horizontal = 24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.kuis_done),
                    contentDescription = "Kuis selesai",
                    modifier = Modifier.fillMaxWidth(0.6f)
                )
            }

            Spacer(Modifier.height(16.dp))
            Text(
                text = "Skormu: $score",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = Jost,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                )
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Selamat! Kamu telah menguasai topik ini.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(32.dp))
            JFPrimaryButton(
                text = "Tutup",
                backgroundColor = BluePrimary,
                onClick = onClose,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}
