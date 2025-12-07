package com.example.jobflick.features.recruiter.discover.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.jobflick.core.ui.components.JFPrimaryButton
import com.example.jobflick.core.ui.theme.GrayInactive
import com.example.jobflick.core.ui.theme.OrangePrimary
import com.example.jobflick.features.recruiter.discover.domain.model.CandidateProfile
import kotlin.math.abs
import kotlin.math.min

@Composable
fun CandidateCardContent(
    candidate: CandidateProfile,
    swipeProgress: Float,
    onSeeDetail: () -> Unit
) {
    val personPainter = rememberVectorPainter(Icons.Filled.Person)

    // Tint waktu swipe (hijau kanan / merah kiri)
    val tintColor: Color? = when {
        swipeProgress > 0f -> Color(0xFF2E7D32)
        swipeProgress < 0f -> Color(0xFFC62828)
        else -> null
    }
    val tintAlpha = min(0.35f, abs(swipeProgress) * 0.5f)

    val labelStyle = MaterialTheme.typography.bodySmall.copy(
        fontWeight = FontWeight.SemiBold
    )
    val valueStyle = MaterialTheme.typography.bodySmall

    // Pisah pendidikan jadi degree (S1) dan jurusan (Sistem Informasi)
    val (degree, major) = splitEducation(candidate.education)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 380.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp) // ruang untuk tombol "Lihat detail"
        ) {
            // =======================
            // Header foto + nama
            // =======================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.LightGray.copy(alpha = 0.4f),
                    modifier = Modifier.size(56.dp)
                ) {
                    if (candidate.photoUrl != null) {
                        AsyncImage(
                            model = candidate.photoUrl,
                            contentDescription = candidate.name,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = personPainter,
                            contentDescription = candidate.name,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxSize()
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                Column {
                    Text(
                        text = candidate.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = candidate.headline,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrayInactive
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            // =====================================================
            // BLOK INFO UTAMA (ROW 1):
            // Level | Pendidikan (S1) | Jurusan (Sistem Informasi)
            // =====================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Kolom 1: Level
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text("Level", style = labelStyle)
                    Text(
                        candidate.level,
                        style = valueStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Kolom 2: Pendidikan (degree saja, misal: S1)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text("Pendidikan", style = labelStyle)
                    Text(
                        degree,
                        style = valueStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Kolom 3: Jurusan (misal: Sistem Informasi)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text("Jurusan", style = labelStyle)
                    Text(
                        major,
                        style = valueStyle,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // =====================================================
            // BLOK INFO TAMBAHAN (ROW 2):
            // Lokasi | Ketersediaan
            // =====================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text("Lokasi", style = labelStyle)
                    Text(
                        candidate.location,
                        style = valueStyle,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text("Ketersediaan", style = labelStyle)
                    Text(
                        candidate.availability,
                        style = valueStyle,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.weight(1f)) // biar grid tetap simetris (3 kolom)
            }

            Spacer(Modifier.height(12.dp))

            // =======================
            // Pengalaman singkat
            // =======================
            Text(
                text = "Pengalaman",
                modifier = Modifier.padding(horizontal = 20.dp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = candidate.experiences.joinToString("\n") {
                    "· ${it.role}, ${it.company} (${it.period})"
                },
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 4.dp),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(8.dp))

            // =======================
            // Keahlian
            // =======================
            Text(
                text = "Keahlian",
                modifier = Modifier.padding(horizontal = 20.dp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = candidate.skills.joinToString(", "),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 4.dp),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Overlay tint merah/hijau saat swipe
        if (tintColor != null && tintAlpha > 0f) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(tintColor.copy(alpha = tintAlpha))
            )
        }

        // Gradient bawah + tombol "Lihat detail"
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(90.dp)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.96f)
                        )
                    )
                )
        )

        JFPrimaryButton(
            text = "Lihat detail",
            backgroundColor = OrangePrimary,
            onClick = onSeeDetail,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        )
    }
}

/**
 * Memecah string pendidikan menjadi:
 *  - degree: kata pertama (misal "S1")
 *  - major : sisa kalimat (misal "Sistem Informasi")
 *
 * Jika tidak bisa di-split, semuanya dianggap degree.
 */
private fun splitEducation(education: String): Pair<String, String> {
    val trimmed = education.trim()
    if (trimmed.isEmpty()) return "" to ""
    val parts = trimmed.split(" ", limit = 2)
    return if (parts.size == 1) {
        parts[0] to ""
    } else {
        parts[0] to parts[1]
    }
}
