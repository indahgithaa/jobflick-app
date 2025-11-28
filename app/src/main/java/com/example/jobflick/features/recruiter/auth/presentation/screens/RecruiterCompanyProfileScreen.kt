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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.InsertPhoto
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
import com.example.jobflick.core.ui.theme.Jost
import com.example.jobflick.core.ui.theme.OrangePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruiterCompanyProfileScreen(
    onPickCompanyLogo: () -> Unit = {},
    onSubmit: (
        companyName: String,
        industry: String,
        employeesCount: String,
        address: String,
        website: String,
        logoFileName: String,
        description: String
    ) -> Unit = { _, _, _, _, _, _, _ -> }
) {
    val context = LocalContext.current

    var companyName by remember { mutableStateOf("") }
    var industry by remember { mutableStateOf("") }
    var employeesCount by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }
    var logoFileName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // kalau nanti mau upload ke backend, Uri ini bisa dikirim ke ViewModel
    var logoUri by remember { mutableStateOf<Uri?>(null) }

    // ==== IMAGE PICKER UNTUK LOGO ====
    val logoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            logoUri = it
            logoFileName = getFileNameFromUri(context, it)
            onPickCompanyLogo()
        }
    }

    val industryOptions = listOf(
        "Teknologi Informasi",
        "Keuangan",
        "Pendidikan",
        "Kesehatan",
        "Manufaktur",
        "Lainnya"
    )
    var industryExpanded by remember { mutableStateOf(false) }

    val isValid = companyName.isNotBlank() &&
            industry.isNotBlank() &&
            employeesCount.isNotBlank() &&
            address.isNotBlank() &&
            website.isNotBlank() &&
            logoFileName.isNotBlank() &&
            description.isNotBlank()

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
                    backgroundColor = if (isValid) OrangePrimary else OrangePrimary.copy(alpha = 0.35f),
                    onClick = {
                        if (isValid) {
                            onSubmit(
                                companyName,
                                industry,
                                employeesCount,
                                address,
                                website,
                                logoFileName,
                                description
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
                    text = "Profil Perusahaan",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                )
                Text(
                    text = "Isi data untuk melengkapi profil perusahaan.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        color = Color.Black.copy(alpha = 0.8f)
                    )
                )
            }

            Spacer(Modifier.height(24.dp))

            SectionLabel("Nama Perusahaan")
            JFInputField(
                modifier = Modifier.padding(horizontal = 24.dp),
                value = companyName,
                onValueChange = { companyName = it },
                placeholder = "Masukkan nama perusahaan"
            )

            Spacer(Modifier.height(12.dp))

            SectionLabel("Industri")
            ExposedDropdownMenuBox(
                modifier = Modifier.padding(horizontal = 24.dp),
                expanded = industryExpanded,
                onExpandedChange = { industryExpanded = !industryExpanded }
            ) {
                TextField(
                    value = industry,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    placeholder = { Text("Pilih salah satu", color = Color(0xFFB7B7B7)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = industryExpanded)
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
                    expanded = industryExpanded,
                    onDismissRequest = { industryExpanded = false }
                ) {
                    industryOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                industry = option
                                industryExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            SectionLabel("Jumlah Karyawan")
            JFInputField(
                modifier = Modifier.padding(horizontal = 24.dp),
                value = employeesCount,
                onValueChange = { employeesCount = it.filter(Char::isDigit) },
                placeholder = "Masukkan jumlah karyawan (dalam angka)",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(Modifier.height(12.dp))

            SectionLabel("Alamat Kantor Utama")
            JFInputField(
                modifier = Modifier.padding(horizontal = 24.dp),
                value = address,
                onValueChange = { address = it },
                placeholder = "Masukkan alamat kantor utama"
            )

            Spacer(Modifier.height(12.dp))

            SectionLabel("Website Resmi Kantor")
            JFInputField(
                modifier = Modifier.padding(horizontal = 24.dp),
                value = website,
                onValueChange = { website = it },
                placeholder = "Masukkan alamat website resmi",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )

            Spacer(Modifier.height(12.dp))

            SectionLabel("Logo Perusahaan")
            CompanyLogoPickerField(
                modifier = Modifier.padding(horizontal = 24.dp),
                fileName = logoFileName,
                placeholder = "Unggah logo perusahaan (JPG/PNG)",
                onPick = {
                    // buka image picker
                    logoPickerLauncher.launch("image/*")
                }
            )

            Spacer(Modifier.height(12.dp))

            SectionLabel("Deskripsi Perusahaan")
            JFInputField(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .height(120.dp),
                value = description,
                onValueChange = { description = it },
                placeholder = "Berikan deskripsi singkat perusahaan (maks. 500 karakter)",
                singleLine = false
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
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

@Composable
private fun CompanyLogoPickerField(
    modifier: Modifier = Modifier,
    fileName: String,
    placeholder: String,
    onPick: () -> Unit
) {
    TextField(
        value = if (fileName.isBlank()) "" else fileName,
        onValueChange = {},
        readOnly = true,
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.InsertPhoto, // MATERIAL ICON sebagai placeholder
                contentDescription = null
            )
        },
        placeholder = { Text(placeholder, color = Color(0xFFB7B7B7)) },
        trailingIcon = {
            IconButton(onClick = onPick) {
                Icon(
                    imageVector = Icons.Outlined.InsertPhoto,
                    contentDescription = null
                )
            }
        },
        modifier = modifier
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
private fun RecruiterCompanyProfilePreview() {
    MaterialTheme {
        RecruiterCompanyProfileScreen()
    }
}
