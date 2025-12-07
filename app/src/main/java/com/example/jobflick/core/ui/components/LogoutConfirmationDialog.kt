package com.example.jobflick.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobflick.R
import com.example.jobflick.core.ui.theme.BluePrimary

@Composable
fun LogoutConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))   // background gelap
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(310.dp)
                .wrapContentHeight()
                .background(Color.White, RoundedCornerShape(24.dp))
                .padding(vertical = 28.dp, horizontal = 24.dp)
                .clickable(enabled = false) {},
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // ICON
                Image(
                    painter = painterResource(id = R.drawable.question_mark_blue),
                    contentDescription = "Question",
                    modifier = Modifier.size(36.dp)
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Apakah yakin untuk logout?",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(32.dp))

                // Tombol Batal + Logout
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    // Batal
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(36.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1f)
                    ) {
                        Text("Batal")
                    }

                    Spacer(Modifier.width(12.dp))

                    // Logout
                    Button(
                        onClick = onConfirm,
                        shape = RoundedCornerShape(36.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BluePrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Logout",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
