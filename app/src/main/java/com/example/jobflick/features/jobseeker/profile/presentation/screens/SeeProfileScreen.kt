package com.example.jobflick.features.jobseeker.profile.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.jobflick.R

@Composable
fun SeeProfileScreen(
    name: String,
    photoUrl: String?,
    whatsapp: String,
    email: String,
    domicile: String,
    education: String,
    cvFileName: String?,
    portfolioUrl: String?,
    onEditField: (String) -> Unit,
    onDeletePortfolio: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // ======================
            // HEADER HALF CIRCLE + AVATAR
            // ======================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp) // lebih pendek supaya curve tidak terlalu turun
            ) {
                Image(
                    painter = painterResource(id = R.drawable.half_blue_profile_circle),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )

                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                )

                // Avatar overlap ke area putih di bawah
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 40.dp) // overlap 40dp ke area putih
                        .size(112.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    if (!photoUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = "Foto profil",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // default sama spirit-nya dengan ProfileHeader
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color(0xFF0059C9).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Foto profil default",
                                tint = Color(0xFF0059C9),
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }
                }
            }

            // Space kompensasi overlap avatar
            Spacer(modifier = Modifier.height(56.dp))

            // ======================
            // FORM PROFIL
            // ======================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {

                ProfileField(
                    label = "Nama Lengkap",
                    value = name,
                    onEdit = { onEditField("name") }
                )

                ProfileField(
                    label = "Nomor WhatsApp",
                    value = whatsapp,
                    onEdit = { onEditField("whatsapp") }
                )

                ProfileField(
                    label = "Email",
                    value = email,
                    onEdit = { onEditField("email") }
                )

                ProfileField(
                    label = "Domisili",
                    value = domicile,
                    onEdit = { onEditField("domicile") }
                )

                ProfileField(
                    label = "Pendidikan Terakhir/Saat Ini",
                    value = education,
                    onEdit = { onEditField("education") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // CV
                Text(
                    text = "CV",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.PictureAsPdf,
                        contentDescription = "CV",
                        tint = Color(0xFF1565C0),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = cvFileName ?: "Belum ada",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Portofolio
                ProfileField(
                    label = "Portofolio",
                    value = portfolioUrl ?: "-",
                    onEdit = { onEditField("portfolio") },
                    trailing = {
                        IconButton(onClick = onDeletePortfolio) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Hapus portofolio",
                                tint = Color(0xFFB0B0B0)
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = onSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD6E6FF),
                        contentColor = Color(0xFF0059C9)
                    ),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = "Simpan",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    onEdit: () -> Unit,
    trailing: (@Composable () -> Unit)? = null
) {
    Column(modifier = Modifier.padding(vertical = 10.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Gray,
                fontSize = 14.sp
            )
        )
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp
                ),
                modifier = Modifier.weight(1f)
            )

            if (trailing != null) {
                trailing()
            } else {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = Color(0xFFB0B0B0),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
