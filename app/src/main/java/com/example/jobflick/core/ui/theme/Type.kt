package com.example.jobflick.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.jobflick.R

val Jost = FontFamily(
    Font(R.font.jost_regular, FontWeight.Normal),
    Font(R.font.jost_bold, FontWeight.Bold),
    Font(R.font.jost_extrabold, FontWeight.ExtraBold)
)

val AppTypography = Typography(
    bodyLarge = Typography().bodyLarge.copy(fontFamily = Jost),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = Jost),
    bodySmall = Typography().bodySmall.copy(fontFamily = Jost)
)

