package com.example.jobflick.features.jobseeker.discover.presentation.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.jobflick.core.ui.components.JFPrimaryButton
import com.example.jobflick.core.ui.theme.BluePrimary
import com.example.jobflick.features.jobseeker.discover.domain.model.JobPosting

@Composable
fun JobMatchScreen(
    job: JobPosting,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val workIconPainter = rememberVectorPainter(Icons.Filled.Work)
    val userIconPainter = rememberVectorPainter(Icons.Filled.Person)

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Tombol close di kiri atas
            IconButton(
                onClick = onClose,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Tutup"
                )
            }

            // Konten utama
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ====== LOGO COMPANY x USER (BERTUMPUK) ======
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Lingkaran logo perusahaan (kiri)
                    Surface(
                        shape = CircleShape,
                        tonalElevation = 2.dp,
                        modifier = Modifier
                            .size(150.dp)
                            .offset(x = (-52).dp)
                    ) {
                        AsyncImage(
                            model = job.logoUrl,
                            contentDescription = job.company,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(),
                            placeholder = workIconPainter,
                            error = workIconPainter
                        )
                    }

                    // Lingkaran user (kanan)
                    Surface(
                        shape = CircleShape,
                        tonalElevation = 2.dp,
                        modifier = Modifier
                            .size(150.dp)
                            .offset(x = 52.dp)
                    ) {
                        Image(
                            painter = userIconPainter,
                            contentDescription = "User",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize()
                        )
                    }

                    // Kalau mau, aktifkan lagi chip biru di tengah:
                    // Surface(
                    //     shape = CircleShape,
                    //     color = BluePrimary,
                    //     shadowElevation = 4.dp,
                    //     modifier = Modifier.size(56.dp)
                    // ) {
                    //     Box(contentAlignment = Alignment.Center) {
                    //         Icon(
                    //             imageVector = Icons.Filled.Work,
                    //             contentDescription = "Match",
                    //             tint = Color.White
                    //         )
                    //     }
                    // }
                }

                Spacer(Modifier.height(32.dp))

                Text(
                    text = "It’s a match!",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Kamu cocok dengan posisi ${job.title} di ${job.company}.",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(Modifier.height(40.dp))

                // Tombol kirim email ke HR
                JFPrimaryButton(
                    text = "Kirim email ke HR",
                    backgroundColor = BluePrimary,
                    onClick = {
                        val hrEmail = "hr@${job.company.replace(" ", "").lowercase()}.com"

                        // Intent utama: ACTION_SENDTO dengan mailto:
                        val mailToUri = Uri.parse("mailto:$hrEmail")
                        val emailIntent = Intent(Intent.ACTION_SENDTO, mailToUri).apply {
                            putExtra(
                                Intent.EXTRA_SUBJECT,
                                "Lamaran untuk ${job.title} di ${job.company}"
                            )
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Halo HR ${job.company},\n\n" +
                                        "Saya tertarik dengan posisi ${job.title}. " +
                                        "Berikut saya lampirkan CV dan portofolio saya.\n\n" +
                                        "Terima kasih."
                            )
                        }

                        val pm = context.packageManager

                        when {
                            emailIntent.resolveActivity(pm) != null -> {
                                // Buka chooser supaya user bisa pilih Gmail/Email lain
                                context.startActivity(
                                    Intent.createChooser(
                                        emailIntent,
                                        "Kirim email menggunakan..."
                                    )
                                )
                            }

                            else -> {
                                // Fallback: ACTION_SEND (misal kalau ACTION_SENDTO gak ada yang handle)
                                val fallback = Intent(Intent.ACTION_SEND).apply {
                                    type = "message/rfc822"
                                    putExtra(Intent.EXTRA_EMAIL, arrayOf(hrEmail))
                                    putExtra(
                                        Intent.EXTRA_SUBJECT,
                                        "Lamaran untuk ${job.title} di ${job.company}"
                                    )
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Halo HR ${job.company},\n\n" +
                                                "Saya tertarik dengan posisi ${job.title}. " +
                                                "Berikut saya lampirkan CV dan portofolio saya.\n\n" +
                                                "Terima kasih."
                                    )
                                }

                                if (fallback.resolveActivity(pm) != null) {
                                    context.startActivity(
                                        Intent.createChooser(
                                            fallback,
                                            "Kirim email menggunakan..."
                                        )
                                    )
                                } else {
                                    Toast
                                        .makeText(
                                            context,
                                            "Tidak ada aplikasi email yang tersedia.",
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
