package com.example.jobflick.features.jobseeker.discover.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.jobflick.R
import com.example.jobflick.features.discover.presentation.components.DiscoverActionButtons
import com.example.jobflick.features.discover.presentation.components.SwipeableCard
import com.example.jobflick.core.ui.theme.GreenApply
import com.example.jobflick.core.ui.theme.RedSkip
import com.example.jobflick.core.ui.theme.YellowUndo
import com.example.jobflick.features.discover.domain.JobPosting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    jobs: List<JobPosting> = sampleJobs(),
    onApply: (JobPosting) -> Unit,
    onSave: (JobPosting) -> Unit,
    onOpenFilter: () -> Unit = {},
) {
    var stack by remember { mutableStateOf(jobs) }
    var lastDiscarded: JobPosting? by remember { mutableStateOf(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Discover") })
        }
    ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        ) {

            Box(Modifier.fillMaxSize()) {
                val cards = stack.take(3).reversed()

                cards.forEachIndexed { idx, job ->
                    val topIndex = cards.lastIndex
                    val isTop = idx == topIndex

                    val layerFromTop = topIndex - idx
                    val scale = when (layerFromTop) {
                        0 -> 1f
                        1 -> 0.96f
                        else -> 0.92f
                    }
                    val offsetY = when (layerFromTop) {
                        0 -> 0.dp
                        1 -> 14.dp
                        else -> 28.dp
                    }

                    SwipeableCard(
                        enabled = isTop,
                        onSwipedLeft = {
                            lastDiscarded = job
                            stack = stack.drop(1)
                        },
                        onSwipedRight = { onApply(job) },
                        onSwipedUp = { onSave(job) },
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .offset(y = offsetY)
                            .graphicsLayer { scaleX = scale; scaleY = scale }
                    ) {
                        JobCardContent(job)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 90.dp)
            ) {
                DiscoverActionButtons(
                    onUndo = {
                        lastDiscarded?.let { recovered ->
                            stack = listOf(recovered) + stack
                            lastDiscarded = null
                        }
                    },
                    onSkip = {
                        if (stack.isNotEmpty()) {
                            lastDiscarded = stack.first()
                            stack = stack.drop(1)
                        }
                    },
                    onApply = {
                        if (stack.isNotEmpty()) onApply(stack.first())
                    },
                    onSave = {
                        if (stack.isNotEmpty()) onSave(stack.first())
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun JobCardContent(job: JobPosting) {
    Column(Modifier.fillMaxWidth().background(Color.White)) {
        // Header
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(job.logoRes),
                contentDescription = job.company,
                modifier = Modifier.size(28.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(job.company, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                Text(job.location, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }

        Text(
            job.title,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(Modifier.height(8.dp))

        Column(Modifier.padding(horizontal = 16.dp)) {
            MetaRow("Skor", "${job.scorePct}% kecocokan",
                valueColor = when {
                    job.scorePct >= 80 -> GreenApply
                    job.scorePct >= 60 -> YellowUndo
                    else -> RedSkip
                })
            MetaRow("Jenis kerja", job.workType)
            MetaRow("Keahlian", job.skills)
            MetaRow("Level", job.level)
            MetaRow("Gaji", job.salary)
        }

        Spacer(Modifier.height(8.dp))
        Text("Tentang Perusahaan",
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
        )
        Text(
            job.about,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(8.dp))
        FilledTonalButton(
            onClick = { /* TODO: detail */ },
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(22.dp)
        ) { Text("Lihat detail") }
    }
}

@Composable
private fun MetaRow(label: String, value: String, valueColor: Color = Color.Unspecified) {
    Row(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray, modifier = Modifier.width(90.dp))
        Text(value, style = MaterialTheme.typography.bodySmall.copy(color = valueColor))
    }
}

// ===== Dummy data (ikon company) =====
@Composable
private fun sampleJobs(): List<JobPosting> = listOf(
    JobPosting(
        company = "Microsoft",
        logoRes = R.drawable.microsoft,
        location = "Jakarta, Indonesia",
        title = "Junior Software Engineer",
        scorePct = 80,
        workType = "Full time, Remote",
        skills = "C++, JavaScript, C#, Python",
        level = "Junior",
        salary = "Rp8–10 jt/bln",
        about = "Microsoft adalah perusahaan teknologi yang mendorong inovasi dan kolaborasi..."
    ),
    JobPosting(
        company = "Gojek",
        logoRes = R.drawable.gojek,
        location = "Jakarta, Indonesia",
        title = "Lead Software Engineer",
        scorePct = 75,
        workType = "Full time, Hybrid",
        skills = "Golang, Java, SQL, Clojure, Ruby",
        level = "Mid",
        salary = "Rp9–12 jt/bln",
        about = "Gojek merupakan platform layanan on-demand terkemuka di Asia Tenggara..."
    )
)
