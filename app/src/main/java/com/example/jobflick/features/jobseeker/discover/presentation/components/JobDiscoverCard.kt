package com.example.jobflick.features.jobseeker.discover.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jobflick.core.common.toTimeAgoLabel
import com.example.jobflick.core.ui.theme.GrayText
import com.example.jobflick.features.jobseeker.discover.domain.model.JobPosting

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JobDiscoverCard(
    job: JobPosting,
    modifier: Modifier = Modifier,
    onSeeDetail: () -> Unit = {}
) {
    val postedLabel = job.postedAt.toTimeAgoLabel()
    val skillsLabel = job.skills.toShortSkillsLabel()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // HEADER: logo + nama perusahaan + lokasi
            Row(verticalAlignment = Alignment.CenterVertically) {

                if (!job.logoUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(job.logoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = job.company,
                        modifier = Modifier.size(40.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = "Company",
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = job.company,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = job.location,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = GrayText
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = job.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Diposting $postedLabel",
                style = MaterialTheme.typography.bodyMedium.copy(color = GrayText)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Deskripsi + blur putih ke bawah
            Box {
                Text(
                    text = job.about,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 4
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White
                                )
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ROW 1: Jenis kerja & Keahlian
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Jenis kerja",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = GrayText
                        )
                    )
                    Text(
                        job.workType,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Keahlian",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = GrayText
                        )
                    )
                    Text(
                        skillsLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ROW 2: Level & Gaji
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Level",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = GrayText
                        )
                    )
                    Text(
                        job.level,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Gaji",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = GrayText
                        )
                    )
                    Text(
                        job.salary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = onSeeDetail) {
                    Text("Lihat detail")
                }
            }
        }
    }
}

/**
 * Convert list skill ke label singkat:
 * 0  -> "-"
 * 1  -> "Node.js"
 * 2+ -> "Node.js, 1+ more" / "Node.js, 2+ more", dst.
 */
private fun List<String>.toShortSkillsLabel(): String {
    if (isEmpty()) return "-"
    if (size == 1) return first()
    val moreCount = size - 1
    return "${first()}, ${moreCount}+ more"
}
