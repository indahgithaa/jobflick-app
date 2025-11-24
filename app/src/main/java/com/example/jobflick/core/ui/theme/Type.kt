package com.example.jobflick.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.jobflick.R

val Jost = FontFamily(
    Font(R.font.jost_regular, FontWeight.Normal),
    Font(R.font.jost_bold, FontWeight.Bold),
    Font(R.font.jost_extrabold, FontWeight.ExtraBold),
    Font(R.font.jost_medium, FontWeight.Medium),
    Font(R.font.jost_semibold, FontWeight.SemiBold),
)

val AppTypography = Typography(
    bodyLarge = Typography().bodyLarge.copy(fontFamily = Jost),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = Jost),
    bodySmall = Typography().bodySmall.copy(fontFamily = Jost),
    headlineLarge = Typography().headlineLarge.copy(fontFamily = Jost),
    headlineMedium = Typography().headlineMedium.copy(fontFamily = Jost),
    headlineSmall = Typography().headlineSmall.copy(fontFamily = Jost),
    titleLarge = Typography().titleLarge.copy(fontFamily = Jost),
    titleMedium = Typography().titleMedium.copy(fontFamily = Jost),
    titleSmall = Typography().titleSmall.copy(fontFamily = Jost),
    labelLarge = Typography().labelLarge.copy(fontFamily = Jost),
    labelMedium = Typography().labelMedium.copy(fontFamily = Jost),
    labelSmall = Typography().labelSmall.copy(fontFamily = Jost),
)

