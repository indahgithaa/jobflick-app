package com.example.jobflick.features.jobseeker.profile.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobflick.core.common.toTimeAgoLabel
import com.example.jobflick.features.jobseeker.profile.domain.model.Job
import com.example.jobflick.features.jobseeker.profile.domain.model.JobCategory
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JobDetailScreen(
    job: Job,
    modifier: Modifier = Modifier,
    onApplyClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(Modifier.height(24.dp))

        DetailHeader(job = job)

        Spacer(Modifier.height(16.dp))

        CompanyRow(job = job)

        Spacer(Modifier.height(24.dp))

        JobInfoRow(job = job)

        Spacer(Modifier.height(24.dp))

        Text(
            text = job.jobTitle,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = job.description,
            style = MaterialTheme.typography.bodyMedium.copy(
                lineHeight = 20.sp
            )
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Kualifikasi Minimum",
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(Modifier.height(8.dp))

        job.minimumQualifications.forEach { item ->
            Text(
                text = "• $item",
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 20.sp
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Tentang Perusahaan",
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = job.aboutCompany,
            style = MaterialTheme.typography.bodyMedium.copy(
                lineHeight = 20.sp
            )
        )

        Spacer(Modifier.height(24.dp))

        if (job.category == JobCategory.SAVED) {
            SavedJobActions(
                onDeleteClick = onDeleteClick,
                onApplyClick = onApplyClick
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DetailHeader(job: Job) {
    Column {
        // Baris status (atas)
        val statusTitle = when (job.category) {
            JobCategory.MATCH -> "Match"
            JobCategory.APPLIED -> "Diproses"
            JobCategory.SAVED -> null
        }

        statusTitle?.let {
            Text(
                text = it,
                color = Color(0xFF0059C9),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(4.dp))
        }

        // Baris bawah: dilamar / diterima / disimpan pada (hari, tanggal, pukul)
        val statusLine = formatStatusLine(job.category, job.statusTimestamp)

        Text(
            text = statusLine,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color(0xFF8E8E8E)
            )
        )
    }
}

@Composable
private fun CompanyRow(job: Job) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    Color(0xFFF2F2F2),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // placeholder logo, sama seperti di list
            Text(
                text = job.companyName.firstOrNull()?.uppercase() ?: "",
                color = Color(0xFF555555),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = job.companyName,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = job.location,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF8E8E8E)
                )
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun JobInfoRow(job: Job) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // KOLOM 1: info posting & level
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Diposting",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = job.postedTimestamp.toTimeAgoLabel(),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF0059C9),
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Level",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = job.level ?: "-",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.width(16.dp))

        // KOLOM 2: jenis kerja & gaji
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Jenis kerja",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = job.workType ?: "-",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Gaji",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = job.salaryRange ?: "-",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.width(16.dp))

        // KOLOM 3: keahlian
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Keahlian",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = job.skills.joinToString(", "),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun SavedJobActions(
    onDeleteClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = onDeleteClick
        ) {
            Text("Hapus")
        }

        Button(
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0059C9),
                contentColor = Color.White
            ),
            onClick = onApplyClick
        ) {
            Text("Lamar")
        }
    }
}

/**
 * Menghasilkan teks:
 * - MATCH   -> "Diterima pada Senin, 25 November 2024, pukul 14.30"
 * - APPLIED -> "Dilamar pada Senin, 25 November 2024, pukul 14.30"
 * - SAVED   -> "Disimpan pada Senin, 25 November 2024, pukul 14.30"
 *
 * `rawTimestamp` diasumsikan format ISO 8601 (OffsetDateTime).
 */
@RequiresApi(Build.VERSION_CODES.O)
private fun formatStatusLine(
    category: JobCategory,
    rawTimestamp: String
): String {
    val prefix = when (category) {
        JobCategory.MATCH -> "Diterima pada"
        JobCategory.APPLIED -> "Dilamar pada"
        JobCategory.SAVED -> "Disimpan pada"
    }

    return try {
        val odt = OffsetDateTime.parse(rawTimestamp)
        val formatter = DateTimeFormatter.ofPattern(
            "EEEE, dd MMMM yyyy, 'pukul' HH.mm",
            Locale("id", "ID")
        )
        val formatted = odt.format(formatter)
        "$prefix $formatted"
    } catch (e: DateTimeParseException) {
        // fallback kalau parsing gagal
        prefix
    }
}
