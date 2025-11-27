package com.example.jobflick.features.recruiter.auth.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobflick.R
import com.example.jobflick.core.ui.components.JFPrimaryButton
import com.example.jobflick.core.ui.theme.Jost
import com.example.jobflick.core.ui.theme.OrangePrimary

@Composable
fun RecruiterDoneScreen(
    onStart: () -> Unit
) {
    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                JFPrimaryButton(
                    text = "Mulai",
                    backgroundColor = OrangePrimary,
                    onClick = onStart
                )
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.recruiter_done),
                contentDescription = null,
                modifier = Modifier
                    .width(260.dp)
                    .height(260.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Kamu sudah siap!",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = Jost,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Temukan talenta yang cocok untukmu sekarang.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = Jost,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}
