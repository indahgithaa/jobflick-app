package com.example.jobflick.features.jobseeker.discover.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.jobflick.R
import com.example.jobflick.core.common.toTimeAgoLabel
import com.example.jobflick.core.global.AppGraph
import com.example.jobflick.core.ui.theme.BluePrimary
import com.example.jobflick.core.ui.theme.GrayInactive
import com.example.jobflick.core.ui.theme.GreenApply
import com.example.jobflick.core.ui.theme.RedSkip
import com.example.jobflick.features.jobseeker.discover.domain.model.JobPosting
import com.example.jobflick.features.jobseeker.discover.presentation.components.DiscoverActionButtons
import com.example.jobflick.features.jobseeker.discover.presentation.components.SwipeableCard
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.min

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    onApply: (JobPosting) -> Unit,
    onSave: (JobPosting) -> Unit,
    onOpenDetail: (JobPosting) -> Unit,
    onOpenFilter: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var stack by remember { mutableStateOf<List<JobPosting>>(emptyList()) }
    var lastDiscarded by remember { mutableStateOf<JobPosting?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // status chip
    var statusMessage by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(statusMessage) {
        if (statusMessage != null) {
            delay(1600)
            statusMessage = null
        }
    }

    // progress swipe kartu teratas
    val density = LocalDensity.current
    val thresholdX = with(density) { 120.dp.toPx() }
    var swipeProgress by remember { mutableStateOf(0f) }

    // load jobs
    LaunchedEffect(Unit) {
        val remote = AppGraph.discoverRemote
        try {
            isLoading = true
            errorMessage = null
            stack = remote.getDiscoverJobs()
        } catch (e: Exception) {
            errorMessage = e.message ?: "Gagal memuat data"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Discover") },
                actions = {
                    IconButton(onClick = onOpenFilter) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = "Filter"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                errorMessage != null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(errorMessage ?: "Terjadi kesalahan")
                    }
                }

                stack.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Tidak ada rekomendasi pekerjaan.")
                    }
                }

                else -> {
                    // ====== STACK KARTU ======
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        val cards = stack.take(3).reversed()

                        cards.forEachIndexed { index, job ->
                            val topIndex = cards.lastIndex
                            val isTop = index == topIndex

                            val layerFromTop = topIndex - index
                            val scale = when (layerFromTop) {
                                0 -> 1f
                                1 -> 0.97f
                                else -> 0.94f
                            }
                            val offsetY = when (layerFromTop) {
                                0 -> 16.dp
                                1 -> 24.dp
                                else -> 32.dp
                            }

                            SwipeableCard(
                                enabled = isTop,
                                onDrag = { x, _ ->
                                    if (isTop) {
                                        swipeProgress = (x / thresholdX).coerceIn(-1f, 1f)
                                    }
                                },
                                onSwipedLeft = {
                                    swipeProgress = 0f
                                    statusMessage = "Lowongan dilewati"
                                    lastDiscarded = job
                                    stack = stack.drop(1)
                                },
                                onSwipedRight = {
                                    swipeProgress = 0f
                                    statusMessage = "Lowongan dilamar"
                                    onApply(job)
                                    lastDiscarded = job
                                    stack = stack.drop(1)
                                },
                                onSwipedUp = {
                                    swipeProgress = 0f
                                    statusMessage = "Lowongan disimpan"
                                    onSave(job)
                                },
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(top = 16.dp)
                                    .offset(y = offsetY)
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                    }
                                    .fillMaxWidth()
                            ) {
                                JobCardContent(
                                    job = job,
                                    swipeProgress = if (isTop) swipeProgress else 0f,
                                    onOpenDetail = { onOpenDetail(job) }
                                )
                            }
                        }
                    }

                    // ===== STATUS CHIP =====
                    if (statusMessage != null) {
                        DiscoverStatusChip(
                            text = statusMessage!!,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 150.dp)
                        )
                    }

                    // ===== BUTTON BAWAH =====
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 70.dp)
                    ) {
                        DiscoverActionButtons(
                            onUndo = {
                                swipeProgress = 0f
                                lastDiscarded?.let { recovered ->
                                    statusMessage = "Lowongan dikembalikan"
                                    stack = listOf(recovered) + stack
                                    lastDiscarded = null
                                }
                            },
                            onSkip = {
                                if (stack.isNotEmpty()) {
                                    swipeProgress = 0f
                                    statusMessage = "Lowongan dilewati"
                                    lastDiscarded = stack.first()
                                    stack = stack.drop(1)
                                }
                            },
                            onApplyClick = {
                                if (stack.isNotEmpty()) {
                                    swipeProgress = 0f
                                    val job = stack.first()
                                    statusMessage = "Lowongan dilamar"
                                    onApply(job)
                                    lastDiscarded = job
                                    stack = stack.drop(1)
                                }
                            },
                            onSaveClick = {
                                if (stack.isNotEmpty()) {
                                    swipeProgress = 0f
                                    statusMessage = "Lowongan disimpan"
                                    onSave(stack.first())
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun JobCardContent(
    job: JobPosting,
    swipeProgress: Float,
    onOpenDetail: () -> Unit
) {
    val subtitleColor = GrayInactive

    val tintColor: Color? = when {
        swipeProgress > 0f -> GreenApply
        swipeProgress < 0f -> RedSkip
        else -> null
    }
    val tintAlpha = min(0.35f, abs(swipeProgress) * 0.5f)

    val workIconPainter = rememberVectorPainter(image = Icons.Filled.Work)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 340.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 56.dp)
        ) {
            // HEADER COMPANY
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = job.logoUrl,
                    contentDescription = job.company,
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Fit,
                    placeholder = workIconPainter,
                    error = workIconPainter
                )

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text = job.company,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = job.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = GrayInactive
                    )
                }
            }

            // TITLE
            Text(
                text = job.title,
                modifier = Modifier.padding(horizontal = 20.dp),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(4.dp))

            // DIPOSTING
            Text(
                text = "Diposting ${job.postedAt.toTimeAgoLabel()}",
                modifier = Modifier.padding(horizontal = 20.dp),
                style = MaterialTheme.typography.bodySmall,
                color = GrayInactive
            )

            Spacer(Modifier.height(12.dp))

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                MetaRow(
                    label = "Jenis kerja",
                    value = job.workType,
                    labelColor = subtitleColor
                )
                MetaRow(
                    label = "Keahlian",
                    value = job.skills.toSkillSummary(),
                    labelColor = subtitleColor
                )
                MetaRow(
                    label = "Level",
                    value = job.level,
                    labelColor = subtitleColor
                )
                MetaRow(
                    label = "Gaji",
                    value = job.salary,
                    labelColor = subtitleColor
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Tentang Perusahaan",
                modifier = Modifier.padding(horizontal = 20.dp),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )

            Text(
                text = job.about,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }

        // TINT MERAH / HIJAU
        if (tintColor != null && tintAlpha > 0f) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(tintColor.copy(alpha = tintAlpha))
            )
        }

        // GRADIENT BAWAH
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(90.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.96f)
                        )
                    )
                )
        )

        // BUTTON LIHAT DETAIL
        FilledTonalButton(
            onClick = onOpenDetail,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = Color(0xFFC6D5EC),
                contentColor = BluePrimary
            )
        ) {
            Text(
                text = "Lihat detail",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun MetaRow(
    label: String,
    value: String,
    labelColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.width(90.dp),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = labelColor
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

/** "Node.js, 3+ more" */
private fun List<String>.toSkillSummary(): String {
    if (isEmpty()) return "-"
    if (size == 1) return first()
    val firstSkill = first()
    val moreCount = size - 1
    return "$firstSkill, ${moreCount}+ more"
}

@Composable
private fun DiscoverStatusChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.Black.copy(alpha = 0.8f),
        shape = RoundedCornerShape(50),
        shadowElevation = 4.dp
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
