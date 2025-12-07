package com.example.jobflick.features.recruiter.discover.presentation.screens

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.jobflick.core.ui.components.RecruiterBottomNavBar
import com.example.jobflick.core.ui.theme.GrayInactive
import com.example.jobflick.core.ui.theme.OrangePrimary
import com.example.jobflick.features.recruiter.discover.data.datasource.RecruiterDiscoverRemoteDataSource
import com.example.jobflick.features.recruiter.discover.data.repository.RecruiterDiscoverRepositoryImpl
import com.example.jobflick.features.recruiter.discover.domain.model.CandidateProfile
import com.example.jobflick.features.recruiter.discover.domain.usecase.GetRecruiterCandidatesUseCase
import com.example.jobflick.features.recruiter.discover.presentation.components.RecruiterDiscoverActionButtons
import com.example.jobflick.features.recruiter.discover.presentation.components.RecruiterSwipeableCard
import com.example.jobflick.features.recruiter.discover.presentation.viewmodel.RecruiterDiscoverViewModel
import kotlinx.coroutines.delay
import kotlin.math.min

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruiterDiscoverScreen(
    currentRoute: String,
    onTabSelected: (String) -> Unit
) {
    // ===== Simple DI =====
    val remote = remember { RecruiterDiscoverRemoteDataSource() }
    val repository = remember { RecruiterDiscoverRepositoryImpl(remote) }
    val getCandidates = remember { GetRecruiterCandidatesUseCase(repository) }
    val viewModel = remember { RecruiterDiscoverViewModel(getCandidates) }

    val uiState by viewModel.uiState.collectAsState()

    var statusMessage by remember { mutableStateOf<String?>(null) }
    var swipeProgress by remember { mutableStateOf(0f) }
    var selectedCandidate by remember { mutableStateOf<CandidateProfile?>(null) }

    // filter menu state
    var filterExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // auto dismiss status chip
    LaunchedEffect(statusMessage) {
        if (statusMessage != null) {
            delay(1600)
            statusMessage = null
        }
    }

    Scaffold(
        topBar = {
            if (selectedCandidate == null) {
                // TopBar Discover (stack kartu)
                TopAppBar(
                    title = {
                        Column {
                            Text("Discover")
                            Text(
                                text = uiState.currentRole,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFB0B0B0)
                            )
                        }
                    },
                    actions = {
                        Box {
                            IconButton(onClick = { filterExpanded = true }) {
                                Icon(
                                    imageVector = Icons.Filled.FilterList,
                                    contentDescription = "Filter"
                                )
                            }

                            DropdownMenu(
                                expanded = filterExpanded,
                                onDismissRequest = { filterExpanded = false }
                            ) {
                                val roles = listOf(
                                    "Junior Software Engineer",
                                    "UI/UX Designer",
                                    "Front-End Developer",
                                    "Backend Developer",
                                    "Data Scientist",
                                    "Data Engineer"
                                )
                                roles.forEach { role ->
                                    DropdownMenuItem(
                                        text = { Text(role) },
                                        onClick = {
                                            filterExpanded = false
                                            viewModel.setRole(role)
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
            } else {
                // TopBar Detail
                TopAppBar(
                    title = { Text("Discover") },
                    navigationIcon = {
                        IconButton(onClick = { selectedCandidate = null }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali")
                        }
                    }
                )
            }
        },
        bottomBar = {
            RecruiterBottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = onTabSelected
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(uiState.errorMessage ?: "Terjadi kesalahan")
                    }
                }

                selectedCandidate != null -> {
                    CandidateDetailContent(
                        candidate = selectedCandidate!!,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        onOpenCv = { url ->
                            url?.let {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse(it))
                                )
                            } ?: Toast
                                .makeText(context, "CV belum tersedia", Toast.LENGTH_SHORT)
                                .show()
                        },
                        onOpenPortfolio = { url ->
                            url?.let {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse(it))
                                )
                            } ?: Toast
                                .makeText(
                                    context,
                                    "Portofolio belum tersedia",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    )
                }

                !uiState.hasCards -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Tidak ada kandidat yang direkomendasikan.")
                    }
                }

                else -> {
                    // ===== Stack kartu kandidat =====
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        val cards = uiState.candidates
                            .drop(uiState.currentIndex)
                            .take(3)
                            .reversed()

                        cards.forEachIndexed { index, candidate ->
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

                            RecruiterSwipeableCard(
                                enabled = isTop,
                                onDrag = { x, _ ->
                                    if (isTop) {
                                        val threshold = 120f
                                        swipeProgress = (x / threshold).coerceIn(-1f, 1f)
                                    }
                                },
                                onSwipedLeft = {
                                    swipeProgress = 0f
                                    statusMessage = "Kandidat dilewati"
                                    viewModel.skipCurrent()
                                },
                                onSwipedRight = {
                                    swipeProgress = 0f
                                    statusMessage = "Kandidat dihubungi"
                                    viewModel.contactCurrent()
                                },
                                onSwipedUp = {
                                    swipeProgress = 0f
                                    statusMessage = "Kandidat disimpan"
                                    viewModel.saveCurrent()
                                },
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(top = 16.dp)
                                    .offset(y = offsetY)
                                    .fillMaxWidth()
                                    .heightIn(min = 380.dp)
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                    }
                            ) {
                                CandidateCardContent(
                                    candidate = candidate,
                                    swipeProgress = if (isTop) swipeProgress else 0f,
                                    onSeeDetail = { selectedCandidate = candidate }
                                )
                            }
                        }
                    }

                    // Status chip
                    if (statusMessage != null) {
                        DiscoverStatusChip(
                            text = statusMessage!!,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 150.dp)
                        )
                    }

                    // Action buttons bawah
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 70.dp)
                    ) {
                        RecruiterDiscoverActionButtons(
                            onUndo = {
                                swipeProgress = 0f
                                viewModel.undoLast()
                                statusMessage = "Perubahan dibatalkan"
                            },
                            onSkip = {
                                swipeProgress = 0f
                                viewModel.skipCurrent()
                                statusMessage = "Kandidat dilewati"
                            },
                            onContact = {
                                swipeProgress = 0f
                                viewModel.contactCurrent()
                                statusMessage = "Kandidat dihubungi"
                            },
                            onSave = {
                                swipeProgress = 0f
                                viewModel.saveCurrent()
                                statusMessage = "Kandidat disimpan"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CandidateDetailContent(
    candidate: CandidateProfile,
    modifier: Modifier = Modifier,
    onOpenCv: (String?) -> Unit,
    onOpenPortfolio: (String?) -> Unit
) {
    val labelStyle = MaterialTheme.typography.bodySmall.copy(
        fontWeight = FontWeight.SemiBold
    )
    val valueStyle = MaterialTheme.typography.bodySmall

    // Pecah pendidikan jadi degree + jurusan (S1 + Sistem Informasi)
    val (degree, major) = splitEducation(candidate.education)

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp) // supaya tombol tidak ketutup navbar
    ) {
        Spacer(Modifier.height(24.dp))

        // Header: foto + nama
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color.LightGray.copy(alpha = 0.4f),
                modifier = Modifier.size(72.dp)
            ) {
                val painter = rememberVectorPainter(Icons.Filled.Person)
                if (candidate.photoUrl != null) {
                    AsyncImage(
                        model = candidate.photoUrl,
                        contentDescription = candidate.name,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painter,
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
                    style = MaterialTheme.typography.headlineSmall.copy(
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

        Spacer(Modifier.height(24.dp))

        // ========= BLOK UTAMA DETAIL (ROW 1)
        // Level | Pendidikan (S1) | Jurusan (Sistem Informasi)
        // ================================================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Kolom 1: Level
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text("Level", style = labelStyle)
                Text(candidate.level, style = valueStyle)
            }

            // Kolom 2: Pendidikan (degree)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text("Pendidikan", style = labelStyle)
                Text(degree, style = valueStyle)
            }

            // Kolom 3: Jurusan
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text("Jurusan", style = labelStyle)
                Text(major, style = valueStyle)
            }
        }

        Spacer(Modifier.height(12.dp))

        // ========= BLOK DETAIL (ROW 2)
        // Lokasi | Ketersediaan
        // ================================================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text("Lokasi", style = labelStyle)
                Text(candidate.location, style = valueStyle)
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text("Ketersediaan", style = labelStyle)
                Text(candidate.availability, style = valueStyle)
            }

            Spacer(modifier = Modifier.weight(1f)) // supaya grid tetap 3 kolom
        }

        Spacer(Modifier.height(24.dp))

        // Pengalaman
        SectionTitle("Pengalaman")
        candidate.experiences.forEach { exp ->
            Text(
                text = "· ${exp.role}, ${exp.company} (${exp.period})",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = exp.description,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        // Sertifikasi
        if (candidate.certifications.isNotEmpty()) {
            SectionTitle("Sertifikasi")
            candidate.certifications.forEach {
                Text(
                    text = "· $it",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
        }

        // Keahlian
        SectionTitle("Keahlian")
        Text(
            text = candidate.skills.joinToString(", "),
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(Modifier.height(12.dp))

        // Bahasa
        if (candidate.languages.isNotEmpty()) {
            SectionTitle("Bahasa yang Dikuasai")
            Text(
                text = candidate.languages.joinToString(", "),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(12.dp))
        }

        // Jenis Kerja
        if (candidate.jobTypes.isNotEmpty()) {
            SectionTitle("Jenis Kerja")
            Text(
                text = candidate.jobTypes.joinToString(", "),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(12.dp))
        }

        // Sistem Kerja
        if (candidate.workSystems.isNotEmpty()) {
            SectionTitle("Sistem Kerja")
            Text(
                text = candidate.workSystems.joinToString(", "),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(12.dp))
        }

        // Gaji
        SectionTitle("Gaji yang Diharapkan")
        Text(
            text = candidate.expectedSalary,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(Modifier.height(20.dp))

        // Kontak
        SectionTitle("Kontak")
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = null,
                tint = GrayInactive
            )
            Spacer(Modifier.width(8.dp))
            Text(candidate.email, style = valueStyle)
        }
        Spacer(Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Phone,
                contentDescription = null,
                tint = GrayInactive
            )
            Spacer(Modifier.width(8.dp))
            Text(candidate.phone, style = valueStyle)
        }

        Spacer(Modifier.height(24.dp))

        // Tombol Lihat CV & Lihat Portofolio
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { onOpenCv(candidate.cvUrl) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(30.dp),
                border = BorderStroke(1.dp, OrangePrimary),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = OrangePrimary
                )
            ) {
                Text("Lihat CV")
            }

            OutlinedButton(
                onClick = { onOpenPortfolio(candidate.portfolioUrl) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(30.dp),
                border = BorderStroke(1.dp, OrangePrimary),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = OrangePrimary
                )
            ) {
                Text("Lihat Portofolio")
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.SemiBold
        ),
        modifier = Modifier.padding(bottom = 4.dp)
    )
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
