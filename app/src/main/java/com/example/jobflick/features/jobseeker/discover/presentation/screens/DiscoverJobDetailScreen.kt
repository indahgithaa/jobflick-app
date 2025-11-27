package com.example.jobflick.features.jobseeker.discover.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.jobflick.core.common.toTimeAgoLabel
import com.example.jobflick.core.ui.theme.GrayInactive
import com.example.jobflick.core.ui.theme.GreenApply
import com.example.jobflick.features.jobseeker.discover.domain.model.JobPosting

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverJobDetailScreen(
    job: JobPosting,
    onBack: () -> Unit,
    onApplyClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val workIconPainter = rememberVectorPainter(Icons.Filled.Work)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Discover") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(Modifier.height(8.dp))

                // HEADER COMPANY
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = job.logoUrl,
                        contentDescription = job.company,
                        modifier = Modifier.size(56.dp),
                        contentScale = ContentScale.Fit,
                        placeholder = workIconPainter,
                        error = workIconPainter
                    )

                    Spacer(Modifier.width(16.dp))

                    Column {
                        Text(
                            text = job.company,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(
                            text = job.location,
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrayInactive
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // META GRID
                MetaGrid(
                    postedLabel = job.postedAt.toTimeAgoLabel(),
                    workType = job.workType,
                    skills = job.skills.toSkillSummary(),
                    level = job.level,
                    salary = job.salary
                )

                Spacer(Modifier.height(24.dp))

                // TITLE
                Text(
                    text = job.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(Modifier.height(12.dp))

                // DESCRIPTION
                Text(
                    text = job.about,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Kualifikasi Minimum",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = job.about,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Tentang Perusahaan",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = job.about,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun MetaGrid(
    postedLabel: String,
    workType: String,
    skills: String,
    level: String,
    salary: String
) {
    Column {
        // Row 1: 3 kolom penuh
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MetaItem(
                title = "Diposting",
                value = postedLabel,
                modifier = Modifier.weight(1f)
            )
            MetaItem(
                title = "Jenis kerja",
                value = workType,
                modifier = Modifier.weight(1f)
            )
            MetaItem(
                title = "Keahlian",
                value = skills,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(16.dp))

        // Row 2: tetap 3 kolom, kolom terakhir dikosongkan
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MetaItem(
                title = "Level",
                value = level,
                modifier = Modifier.weight(1f)
            )
            MetaItem(
                title = "Gaji",
                value = salary,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.weight(1f)) // kolom kosong biar grid tetap imbang
        }
    }
}

@Composable
private fun MetaItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

private fun List<String>.toSkillSummary(): String {
    if (isEmpty()) return "-"
    if (size == 1) return first()
    val first = first()
    val more = size - 1
    return "$first, ${more}+ more"
}
