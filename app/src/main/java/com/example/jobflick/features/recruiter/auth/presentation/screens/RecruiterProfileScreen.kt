package com.example.jobflick.features.recruiter.auth.presentation.screens

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.InsertPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobflick.R
import com.example.jobflick.core.ui.components.JFInputField
import com.example.jobflick.core.ui.components.JFPrimaryButton
import com.example.jobflick.core.ui.theme.Jost
import com.example.jobflick.core.ui.theme.OrangePrimary

@Composable
fun RecruiterProfileInfoScreen(
    onPickPhoto: () -> Unit = {},
    onSubmit: (
        fullName: String,
        position: String,
        workEmail: String,
        phone: String,
        photoFileName: String
    ) -> Unit = { _, _, _, _, _ -> }
) {
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var workEmail by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var photoFileName by remember { mutableStateOf("") }

    // kalau nanti mau upload ke backend, Uri ini bisa dilempar ke ViewModel
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    // ==== IMAGE PICKER UNTUK FOTO PROFIL ====
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            photoUri = it
            photoFileName = getFileNameFromUri(context, it)
            onPickPhoto()
        }
    }

    val isValid = fullName.isNotBlank() &&
            position.isNotBlank() &&
            workEmail.isNotBlank() &&
            phone.isNotBlank()
    // catatan: foto tetap opsional → tidak masuk ke isValid

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                JFPrimaryButton(
                    text = "Selesai",
                    backgroundColor = if (isValid) OrangePrimary else OrangePrimary.copy(alpha = 0.35f),
                    onClick = {
                        if (isValid) {
                            onSubmit(
                                fullName,
                                position,
                                workEmail,
                                "+62$phone",
                                photoFileName
                            )
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.wavy_left_icon_header_orange),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Column(Modifier.padding(horizontal = 24.dp)) {
                Text(
                    text = "Profil Rekruter",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                )
                Text(
                    text = "Isi data untuk melengkapi informasi rekruter.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        color = Color.Black.copy(alpha = 0.8f)
                    )
                )
            }

            Spacer(Modifier.height(24.dp))

            // Foto profil rekruter (Material icon sebagai placeholder)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .clickable {
                            // buka image picker
                            photoPickerLauncher.launch("image/*")
                        },
                    color = Color.White,
                    tonalElevation = 2.dp,
                    shadowElevation = 4.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.InsertPhoto,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Foto Profil",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = Jost,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = if (photoFileName.isBlank()) "Unggah foto profil (opsional)"
                        else photoFileName,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = Jost,
                            color = Color.Gray
                        )
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Label("Nama Lengkap")
            JFInputField(
                modifier = Modifier.padding(horizontal = 24.dp),
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Masukkan nama lengkap"
            )

            Spacer(Modifier.height(12.dp))

            Label("Jabatan")
            JFInputField(
                modifier = Modifier.padding(horizontal = 24.dp),
                value = position,
                onValueChange = { position = it },
                placeholder = "Tambahkan posisi atau jabatan"
            )

            Spacer(Modifier.height(12.dp))

            Label("Email Kantor")
            JFInputField(
                modifier = Modifier.padding(horizontal = 24.dp),
                value = workEmail,
                onValueChange = { workEmail = it },
                placeholder = "Masukkan email kantor",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(Modifier.height(12.dp))

            Label("Kontak Aktif")
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = "+62",
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier
                        .width(64.dp)
                        .height(55.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = Color.Transparent
                    )
                )
                Spacer(Modifier.width(8.dp))
                JFInputField(
                    value = phone,
                    onValueChange = { phone = it.filter(Char::isDigit) },
                    placeholder = "8xxxxxxxxxx",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 24.dp),
        style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.SemiBold,
            fontFamily = Jost,
            color = Color.Black
        )
    )
    Spacer(Modifier.height(8.dp))
}

/**
 * Helper untuk ambil nama file dari Uri
 */
private fun getFileNameFromUri(context: Context, uri: Uri): String {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (nameIndex != -1 && it.moveToFirst()) {
            return it.getString(nameIndex)
        }
    }
    // fallback kalau gagal ambil DISPLAY_NAME
    return uri.lastPathSegment ?: "file"
}

@Preview(showBackground = true, device = "spec:width=411dp,height=1600dp,dpi=420")
@Composable
private fun RecruiterProfileInfoPreview() {
    MaterialTheme {
        RecruiterProfileInfoScreen()
    }
}
