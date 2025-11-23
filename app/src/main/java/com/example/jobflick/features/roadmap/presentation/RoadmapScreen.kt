package com.example.jobflick.features.roadmap.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme

@Composable
fun RoadmapScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Roadmap",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 28.sp
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Halaman roadmap kamu akan tampil di sini.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
