package com.example.jobflick.features.jobseeker.profile.presentation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobflick.features.jobseeker.profile.presentation.components.SettingsItem

@Composable
fun ProfileSettingsScreen(
    onOpenProfile: () -> Unit,
    onOpenExperience: () -> Unit,
    onOpenPreference: () -> Unit,
    onOpenSecurity: () -> Unit,
    onOpenDeleteAccount: () -> Unit,
    onToggleDirectContact: (Boolean) -> Unit,
    onOpenAboutApp: () -> Unit,
    onOpenReport: () -> Unit,
    onOpenTerms: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var directContactEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 26.sp
                ),
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 18.dp)
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {

            // ========= PENGATURAN AKUN =========
            Text(
                text = "Pengaturan Akun",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )

            SettingsItem(
                icon = Icons.Filled.Person,
                text = "Profil",
                onClick = onOpenProfile
            )

            SettingsItem(
                icon = Icons.Filled.WorkOutline,
                text = "Pengalaman",
                onClick = onOpenExperience
            )

            SettingsItem(
                icon = Icons.Filled.Info,
                text = "Preferensi",
                onClick = onOpenPreference
            )

            SettingsItem(
                icon = Icons.Filled.Lock,
                text = "Keamanan Akun",
                onClick = onOpenSecurity
            )

            SettingsItem(
                icon = Icons.Filled.DeleteOutline,
                text = "Hapus Akun",
                onClick = onOpenDeleteAccount
            )

            SettingsItem(
                icon = Icons.Filled.Call,
                text = "Aktifkan Langsung Kontak",
                trailing = {
                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .wrapContentWidth()
                    ) {
                        Switch(
                            checked = directContactEnabled,
                            onCheckedChange = {
                                directContactEnabled = it
                                onToggleDirectContact(it)
                            },
                            modifier = Modifier
                                .scale(0.75f)
                                .align(Alignment.Center)
                        )
                    }
                }
            )


            // ========= TENTANG JOBFLICK =========
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Tentang JobFlick",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            SettingsItem(
                icon = Icons.Filled.Info,
                text = "Tentang Aplikasi",
                onClick = onOpenAboutApp
            )

            SettingsItem(
                icon = Icons.Filled.ReportProblem,
                text = "Beri Laporan",
                onClick = onOpenReport
            )

            SettingsItem(
                icon = Icons.Filled.Description,
                text = "Syarat, Ketentuan, dan Privasi",
                onClick = onOpenTerms
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE57373),
                        shape = RoundedCornerShape(14.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFFE53935)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Keluar",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
