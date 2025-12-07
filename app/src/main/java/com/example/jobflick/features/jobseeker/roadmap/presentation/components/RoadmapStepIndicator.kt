package com.example.jobflick.features.jobseeker.roadmap.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jobflick.core.ui.theme.BluePrimary

@Composable
fun RoadmapStepIndicator(
    number: Int,
    backgroundColor: Color = BluePrimary,
    contentColor: Color = Color.White
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(backgroundColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            color = contentColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}
