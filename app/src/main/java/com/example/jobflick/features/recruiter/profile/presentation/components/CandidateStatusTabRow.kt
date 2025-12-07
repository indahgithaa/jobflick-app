package com.example.jobflick.features.recruiter.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jobflick.features.recruiter.profile.domain.model.CandidateCategory

@Composable
fun CandidateStatusTabRow(
    selected: CandidateCategory,
    onSelected: (CandidateCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = Color(0xFFF2F2F2)

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = bg,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CandidateCategory.values().forEach { category ->
                val title = when (category) {
                    CandidateCategory.MATCH -> "Match"
                    CandidateCategory.SAVED -> "Tersimpan"
                    CandidateCategory.CONTACTED -> "Dihubungi"
                }

                val isSelected = category == selected
                val textColor = if (isSelected) Color.Black else Color(0xFF999999)
                val fontWeight =
                    if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                val tabBg =
                    if (isSelected) Color.White else Color.Transparent

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .background(tabBg, RoundedCornerShape(18.dp))
                        .clickable { onSelected(category) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = fontWeight
                    )
                }
            }
        }
    }
}
