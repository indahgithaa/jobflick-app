package com.example.jobflick.features.recruiter.postjob.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.jobflick.core.ui.components.JFInputField
import com.example.jobflick.core.ui.components.JFPrimaryButton
import com.example.jobflick.core.ui.components.RecruiterBottomNavBar
import com.example.jobflick.core.ui.theme.Jost
import com.example.jobflick.core.ui.theme.OrangePrimary
import com.example.jobflick.features.recruiter.postjob.domain.model.RecruiterJobPostRequest
import com.example.jobflick.features.recruiter.postjob.presentation.RecruiterPostJobUiState
import com.example.jobflick.features.recruiter.postjob.presentation.RecruiterPostJobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruiterPostJobScreen(
    currentRoute: String,
    uiState: RecruiterPostJobUiState,
    viewModel: RecruiterPostJobViewModel,
    onTabSelected: (String) -> Unit,
    onBack: () -> Unit,
    onSubmitSuccess: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var level by remember { mutableStateOf("") }
    var jobType by remember { mutableStateOf("") }
    var workSystem by remember { mutableStateOf("") }
    var minSalary by remember { mutableStateOf("") }
    var maxSalary by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var minQualifications by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Buat Lowongan Baru") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // ===== Posisi/Jabatan =====
                FieldLabel(text = "Posisi/Jabatan")
                JFInputField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "Masukkan posisi yang dicari"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ===== Level =====
                FieldLabel(text = "Level")
                JFInputField(
                    value = level,
                    onValueChange = { level = it },
                    placeholder = "Masukkan level posisi yang dicari"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ===== Jenis Kerja =====
                FieldLabel(text = "Jenis Kerja")
                JFInputField(
                    value = jobType,
                    onValueChange = { jobType = it },
                    placeholder = "Masukkan jenis kerja"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ===== Sistem Kerja =====
                FieldLabel(text = "Sistem Kerja")
                JFInputField(
                    value = workSystem,
                    onValueChange = { workSystem = it },
                    placeholder = "Masukkan sistem kerja"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ===== Gaji =====
                FieldLabel(text = "Gaji")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    JFInputField(
                        value = minSalary,
                        onValueChange = { minSalary = it },
                        modifier = Modifier.weight(1f),
                        placeholder = "Gaji minimum"
                    )
                    JFInputField(
                        value = maxSalary,
                        onValueChange = { maxSalary = it },
                        modifier = Modifier.weight(1f),
                        placeholder = "Gaji maksimum"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ===== Keahlian yang Dibutuhkan =====
                FieldLabel(text = "Keahlian yang Dibutuhkan")
                JFInputField(
                    value = skills,
                    onValueChange = { skills = it },
                    placeholder = "Masukkan keahlian yang dibutuhkan"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ===== Deskripsi Pekerjaan (multiline, tinggi) =====
                FieldLabel(text = "Deskripsi Pekerjaan")
                BigMultilineTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = "Masukkan deskripsi pekerjaan"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ===== Kualifikasi Minimum (multiline, tinggi) =====
                FieldLabel(text = "Kualifikasi Minimum")
                BigMultilineTextField(
                    value = minQualifications,
                    onValueChange = { minQualifications = it },
                    placeholder = "Masukkan kualifikasi minimum dalam bentuk perpoin"
                )

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    JFPrimaryButton(
                        text = if (uiState.isSubmitting) "Mengunggah..." else "Unggah",
                        backgroundColor = OrangePrimary,
                        onClick = {
                            if (uiState.isSubmitting) return@JFPrimaryButton

                            val request = RecruiterJobPostRequest(
                                title = title,
                                level = level,
                                jobType = jobType,
                                workSystem = workSystem,
                                minSalary = minSalary.toLongOrNull(),
                                maxSalary = maxSalary.toLongOrNull(),
                                isNegotiable = false,
                                skills = skills
                                    .split(",")
                                    .map { it.trim() }
                                    .filter { it.isNotEmpty() },
                                description = description,
                                minQualifications = minQualifications
                            )

                            viewModel.submitJob(
                                request = request,
                                onSuccess = onSubmitSuccess
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            if (uiState.isSubmitting) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

/* ==== Helper composable kecil buat label dan textfield besar ==== */

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        fontFamily = Jost,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = Color.Black
    )
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun BigMultilineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp),           // lebih tinggi dari field biasa
        singleLine = false,
        textStyle = LocalTextStyle.current.copy(
            fontFamily = Jost,
            fontSize = 16.sp
        ),
        placeholder = {
            Text(
                text = placeholder,
                fontFamily = Jost,
                fontSize = 16.sp,
                color = Color(0xFFB7B7B7)
            )
        },
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color(0xFFDDDDDD),
            unfocusedIndicatorColor = Color(0xFFE6E6E6),
            disabledIndicatorColor = Color(0xFFE6E6E6),
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedPlaceholderColor = Color(0xFFB7B7B7),
            unfocusedPlaceholderColor = Color(0xFFB7B7B7)
        )
    )
}
