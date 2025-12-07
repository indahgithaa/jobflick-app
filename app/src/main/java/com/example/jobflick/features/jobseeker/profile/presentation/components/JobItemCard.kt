package com.example.jobflick.features.jobseeker.profile.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jobflick.core.common.toTimeAgoLabel
import com.example.jobflick.features.jobseeker.profile.domain.model.Job

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JobItemCard(
    job: Job,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE5E5E5),
                shape = RoundedCornerShape(16.dp)
            )
            .background(Color.White)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // LOGO / ICON
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Work,
                contentDescription = null,
                tint = Color(0xFF757575)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = job.jobTitle,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF202124)
                )
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = job.companyName,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF7A7A7A)
                )
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(horizontalAlignment = Alignment.End) {
            // waktu statusnya (saved/applied/match) relatif, mis. "2 hari lalu"
            Text(
                text = job.statusTimestamp.toTimeAgoLabel(),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFFB0B0B0)
                )
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Lihat detail",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF0066CC),
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
