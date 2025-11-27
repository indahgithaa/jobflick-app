package com.example.jobflick.features.jobseeker.message.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme

@Composable
fun MessageScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Messages",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 28.sp
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Semua pesan & percakapan akan muncul di sini.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
