package com.example.jobflick.features.jobseeker.roadmap.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jobflick.core.ui.components.JFLinearProgress
import com.example.jobflick.core.ui.theme.BluePrimary
import com.example.jobflick.features.jobseeker.roadmap.domain.model.RoadmapModule
import com.example.jobflick.features.jobseeker.roadmap.presentation.components.QuizFinishDialogCustom
import com.example.jobflick.features.jobseeker.roadmap.presentation.components.RoadmapTopBar

@Composable
fun QuizScreen(
    module: RoadmapModule,
    onBack: () -> Unit,
    // ⬇️ tetap kirim LIST INDEX JAWABAN, skor dihitung di BE (NavGraph + repository)
    onQuizFinished: (List<Int>) -> Unit
) {
    val questions = module.questions
    if (questions.isEmpty()) {
        Scaffold(
            topBar = {
                RoadmapTopBar(
                    title = "Kuis",
                    subtitle = module.title,
                    onBack = onBack
                )
            }
        ) { inner ->
            Box(
                modifier = Modifier
                    .padding(inner)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Belum ada soal untuk kuis ini.")
            }
        }
        return
    }

    var currentQuestionIndex by remember { mutableStateOf(0) }
    // key = index soal, value = index opsi yang dipilih
    var selectedAnswers by remember { mutableStateOf<Map<Int, Int>>(emptyMap()) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val total = questions.size
    val currentQuestion = questions[currentQuestionIndex]
    val currentSelectedIndex = selectedAnswers[currentQuestionIndex]

    Scaffold(
        topBar = {
            RoadmapTopBar(
                title = "Kuis",
                subtitle = module.title,
                onBack = onBack
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxSize()
        ) {

            // ================== PROGRESS ==================
            JFLinearProgress(
                progress = (currentQuestionIndex + 1f) / total.toFloat(),
                progressColor = BluePrimary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            QuizStepIndicator(
                current = currentQuestionIndex + 1,
                total = total
            )

            Spacer(Modifier.height(24.dp))

            // ================== BODY ==================
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Soal ${currentQuestionIndex + 1} dari $total",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = currentQuestion.question,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(Modifier.height(24.dp))

                currentQuestion.options.forEachIndexed { index, option ->
                    QuizOptionItem(
                        text = option,
                        isSelected = currentSelectedIndex == index,
                        onClick = {
                            selectedAnswers = selectedAnswers.toMutableMap().apply {
                                this[currentQuestionIndex] = index
                            }
                        }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }

            Spacer(Modifier.height(16.dp))

            // ================== BOTTOM BUTTONS ==================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                QuizRoundedButton(
                    text = "< Sebelumnya",
                    enabled = currentQuestionIndex > 0,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        if (currentQuestionIndex > 0) {
                            currentQuestionIndex--
                        }
                    }
                )

                QuizRoundedButton(
                    text = if (currentQuestionIndex == total - 1) "Selesai >" else "Selanjutnya >",
                    enabled = true,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        if (currentQuestionIndex == total - 1) {
                            // soal terakhir → konfirmasi, lalu kirim jawaban ke luar
                            showConfirmDialog = true
                        } else {
                            currentQuestionIndex++
                        }
                    }
                )
            }
        }

        if (showConfirmDialog) {
            QuizFinishDialogCustom(
                onDismiss = { showConfirmDialog = false },
                onConfirm = {
                    showConfirmDialog = false

                    // susun list jawaban sesuai urutan soal (dipakai BE buat hitung skor)
                    val answers: List<Int> = questions.mapIndexed { index, _ ->
                        selectedAnswers[index] ?: -1 // -1 = tidak menjawab
                    }
                    onQuizFinished(answers)
                }
            )
        }
    }
}

@Composable
private fun QuizStepIndicator(
    current: Int,
    total: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(total) { idx ->
            val step = idx + 1
            val isActive = step == current

            Box(
                modifier = Modifier
                    .width(40.dp)
                    .background(
                        color = if (isActive) BluePrimary else Color(0xFFE0E0E0),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = step.toString(),
                    color = if (isActive) Color.White else Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun QuizOptionItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) BluePrimary else Color(0xFFD5D5D5)
    val background = if (isSelected) Color(0xFFE7F0FF) else Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(background, RoundedCornerShape(18.dp))
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun QuizRoundedButton(
    text: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val bgColor = if (enabled) BluePrimary else Color(0xFFE0E0E0)
    val textColor = Color.White

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(47.dp),
        shape = RoundedCornerShape(36.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            contentColor = textColor,
            disabledContainerColor = bgColor,
            disabledContentColor = textColor
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = textColor
        )
    }
}
