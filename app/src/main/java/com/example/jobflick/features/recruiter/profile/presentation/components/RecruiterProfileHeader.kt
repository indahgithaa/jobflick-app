package com.example.jobflick.features.recruiter.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobflick.core.ui.theme.Jost
import com.example.jobflick.core.ui.theme.OrangePrimary
import com.example.jobflick.features.recruiter.profile.domain.model.RecruiterProfile

@Composable
fun RecruiterProfileHeader(
    profile: RecruiterProfile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        OrangePrimary,
                        OrangePrimary.copy(alpha = 0.95f)
                    )
                )
            )
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // avatar bulat putih dengan placeholder sederhana
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                // placeholder kotak-kotak ala logo di desain bisa dibuat simpel
                Row(
                    modifier = Modifier
                        .size(40.dp)
                ) {
                    Column(Modifier.weight(1f)) {
                        Box(
                            Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .background(Color(0xFFF15A29))
                        )
                        Box(
                            Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .background(Color(0xFF00A0E4))
                        )
                    }
                    Spacer(Modifier.width(2.dp))
                    Column(Modifier.weight(1f)) {
                        Box(
                            Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .background(Color(0xFF8CBF26))
                        )
                        Box(
                            Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .background(Color(0xFFFBB800))
                        )
                    }
                }
            }

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    text = profile.name,
                    fontFamily = Jost,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Lihat profil",
                    fontFamily = Jost,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}
