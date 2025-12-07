package com.example.jobflick.features.auth.presentation

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.jobflick.core.ui.theme.BluePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompleteProfileScreen(
    role: String,
    // callback tetap ada, dipanggil setelah user pilih file (tanpa mengubah signature)
    onPickPhoto: () -> Unit = {},
    onPickCV: () -> Unit = {},
    onSubmit: (
        name: String,
        phone: String,
        email: String,
        domicile: String,
        education: String,
        major: String,
        portfolio: String?,
        photoName: String,
        cvName: String
    ) -> Unit = { _, _, _, _, _, _, _, _, _ -> }
) {
    val context = LocalContext.current

    // ---- State ----
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var domicile by remember { mutableStateOf("") }
    var education by remember { mutableStateOf("") }
    var major by remember { mutableStateOf("") }
    var portfolio by remember { mutableStateOf("") }
    var photoName by remember { mutableStateOf("") }
    var cvName by remember { mutableStateOf("") }

    // (opsional) kalau nanti mau dipakai upload ke server, bisa simpan Uri juga
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var cvUri by remember { mutableStateOf<Uri?>(null) }

    // ==== LAUNCHER PICKER ====
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            photoUri = it
            photoName = getFileNameFromUri(context, it)
            onPickPhoto()
        }
    }

    val cvPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            cvUri = it
            cvName = getFileNameFromUri(context, it)
            onPickCV()
        }
    }

    val eduOptions = listOf(
        "SMP/Sederajat", "SMA/SMK/Sederajat",
        "Diploma 1 (D1)", "Diploma 2 (D2)", "Diploma 3 (D3)",
        "Sarjana (S1)", "Profesi", "Magister (S2)", "Doktor (S3)"
    )
    var eduExpanded by remember { mutableStateOf(false) }

    val isValid = name.isNotBlank() && phone.isNotBlank() &&
            email.contains("@") && domicile.isNotBlank() &&
            education.isNotBlank() && major.isNotBlank() &&
            photoName.isNotBlank() && cvName.isNotBlank()

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                JFPrimaryButton(
                    text = "Lanjut",
                    backgroundColor = if (isValid) BluePrimary else BluePrimary.copy(alpha = 0.35f),
                    onClick = {
                        if (isValid) {
                            onSubmit(
                                name,
                                "+62$phone",
                                email,
                                domicile,
                                education,
                                major,
                                portfolio.ifBlank { null },
                                photoName,
                                cvName
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
            // ===== Header full-bleed tanpa padding =====
            Image(
                painter = painterResource(id = R.drawable.wavy_left_icon_header),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // ===== Headline =====
            Column(Modifier.padding(horizontal = 24.dp)) {
                Text(
                    text = "Lengkapi Profil",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                )
                Text(
                    text = "Isi data diri untuk melengkapi profil.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        color = Color.Black.copy(alpha = 0.8f)
                    )
                )
            }

            Spacer(Modifier.height(24.dp))

            // ===== Nama Lengkap =====
            Labeled(text = "Nama Lengkap")
            JFInputField(
                modifier = Modifier.padding(horizontal = 24.dp),
                value = name,
                onValueChange = { name = it },
                placeholder = "Masukkan nama lengkap"
            )

            Spacer(Modifier.height(12.dp))

            // ===== Foto Diri =====
            FilePickerField(
                modifier = Modifier.padding(horizontal = 24.dp),
                label = "Foto Diri",
                fileName = photoName,
                placeholder = "Unggah foto diri (JPG/PNG)",
                onPick = {
                    // buka image picker
                    photoPickerLauncher.launch("image/*")
                }
            )

            Spacer(Modifier.height(12.dp))

            // ===== Nomor WhatsApp =====
            Labeled(text = "Nomor WhatsApp")
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // +62 fixed, readOnly, gaya mirip JFInputField (filled, rounded)
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
                    placeholder = "81234567890",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(12.dp))

            // ===== Email =====
            Labeled(text = "Email")
            JFInputField(
                modifier = Modifier.padding(horizontal = 24.dp),
                value = email,
                onValueChange = { email = it },
                placeholder = "Masukkan email",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(Modifier.height(12.dp))

            // ===== Domisili =====
            Labeled(text = "Domisili")
            JFInputField(
                modifier = Modifier.padding(horizontal = 24.dp),
                value = domicile,
                onValueChange = { domicile = it },
                placeholder = "Masukkan kota tinggal saat ini"
            )

            Spacer(Modifier.height(12.dp))

            // ===== Pendidikan =====
            Labeled(text = "Pendidikan Terakhir/Saat ini")
            ExposedDropdownMenuBox(
                modifier = Modifier.padding(horizontal = 24.dp),
                expanded = eduExpanded,
                onExpandedChange = { eduExpanded = !eduExpanded }
            ) {
                // TextField filled agar match style JFInputField
                TextField(
                    value = education,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    placeholder = { Text("Pilih salah satu", color = Color(0xFFB7B7B7)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = eduExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
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
                    )
                )

                ExposedDropdownMenu(
                    expanded = eduExpanded,
                    onDismissRequest = { eduExpanded = false }
                ) {
                    eduOptions.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                education = it
                                eduExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // ===== Program Studi/Jurusan =====
            Labeled(text = "Program Studi/Jurusan")
            JFInputField(
                modifier = Modifier.padding(horizontal = 24.dp),
                value = major,
                onValueChange = { major = it },
                placeholder = "Masukkan jurusan"
            )

            Spacer(Modifier.height(12.dp))

            // ===== CV =====
            FilePickerField(
                modifier = Modifier.padding(horizontal = 24.dp),
                label = "CV",
                fileName = cvName,
                placeholder = "Unggah CV dalam format PDF",
                onPick = {
                    // buka file picker khusus PDF
                    cvPickerLauncher.launch("application/pdf")
                }
            )

            Spacer(Modifier.height(12.dp))

            // ===== Portofolio (opsional) =====
            Labeled(text = "Portofolio (opsional)")
            JFInputField(
                modifier = Modifier.padding(horizontal = 24.dp),
                value = portfolio,
                onValueChange = { portfolio = it },
                placeholder = "Tempel link URL"
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

/**
 * Label kecil tebal hitam untuk ditempatkan di atas input (konsisten)
 */
@Composable
private fun Labeled(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 24.dp),
        style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    )
    Spacer(Modifier.height(8.dp))
}

/**
 * FilePickerField tanpa border, gaya mirip JFInputField (filled + rounded)
 */
@Composable
private fun FilePickerField(
    modifier: Modifier = Modifier,
    label: String,
    fileName: String,
    placeholder: String,
    onPick: () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        )
        Spacer(Modifier.height(4.dp))
        TextField(
            value = if (fileName.isBlank()) "" else fileName,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            placeholder = { Text(placeholder, color = Color(0xFFB7B7B7)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onPick() }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clickable { onPick() },
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
            )
        )
    }
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

@Preview(
    name = "CompleteProfileScreen - Jobseeker",
    showBackground = true,
    device = "spec:width=411dp,height=1600dp,dpi=420"
)
@Composable
private fun PreviewCompleteProfileJobseeker() {
    MaterialTheme {
        CompleteProfileScreen(
            role = "jobseeker",
            onPickPhoto = { /* noop */ },
            onPickCV = { /* noop */ },
            onSubmit = { _, _, _, _, _, _, _, _, _ -> /* noop */ }
        )
    }
}
