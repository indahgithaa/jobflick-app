package com.example.jobflick.features.onboarding.presentation

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobflick.R
import com.example.jobflick.core.ui.theme.Jost
import com.example.jobflick.core.ui.theme.BluePrimary
import com.example.jobflick.core.ui.theme.OrangePrimary
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun RoleSelectionScreen(
    onJobSeeker: () -> Unit,
    onRecruiter: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val painter = painterResource(R.drawable.wavy_center_icon_header)
        val ratio = if (painter.intrinsicSize.isUnspecified) 16f / 9f
        else painter.intrinsicSize.width / painter.intrinsicSize.height

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio),
            contentScale = ContentScale.FillWidth
        )

        Spacer(Modifier.height(36.dp))
        Text(
            text = "Lanjutkan sebagai",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 45.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontFamily = Jost,
                fontWeight = FontWeight.Black,
                fontSize = 26.sp,
                lineHeight = 35.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(15.dp))
        Text(
            text = "Apakah kamu sedang mencari pekerjaan atau ingin merekrut pekerja? Pilih peran untuk memulai.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 45.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = Jost,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
            )
        )

        Spacer(Modifier.height(70.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 45.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RolePillButton(
                text = "Jobseeker",
                icon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) },
                background = BluePrimary,
                onClick = onJobSeeker
            )
            Spacer(Modifier.height(16.dp))
            RolePillButton(
                text = "Recruiter",
                icon = {Icon(
                    painter = painterResource(id = R.drawable.work_outlined),
                    contentDescription = null,
                    tint = Color.White
                )},
                background = OrangePrimary,
                onClick = onRecruiter
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun RolePillButton(
    text: String,
    icon: @Composable () -> Unit,
    background: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(36.dp)),
        shape = RoundedCornerShape(36.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = background,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            icon()
            Spacer(Modifier.width(10.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = Jost,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            )
        }
    }
}

@Preview(
    showBackground = true,
)

@Composable
private fun RoleSelectionPreview() {
    Surface {
        RoleSelectionScreen(onJobSeeker = {}, onRecruiter = {})
    }
}