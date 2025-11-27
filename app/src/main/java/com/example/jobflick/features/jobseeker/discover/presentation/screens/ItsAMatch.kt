package com.example.jobflick.features.jobseeker.discover.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jobflick.features.discover.domain.JobPosting

@Composable
fun ItsAMatchScreen(
    job: JobPosting,
    onSeeDetails: () -> Unit,
    onBackToDiscover: () -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(job.logoRes),
                    contentDescription = job.company,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text("It's a Match!", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold))
                Spacer(Modifier.height(6.dp))
                Text(job.title, style = MaterialTheme.typography.titleMedium)
                Text(job.company, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(Modifier.height(18.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onSeeDetails) { Text("Lihat detail") }
                    Button(onClick = onBackToDiscover) { Text("Kembali") }
                }
            }
        }
    }
}
