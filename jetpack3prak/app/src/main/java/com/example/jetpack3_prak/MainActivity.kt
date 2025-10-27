package com.example.jetpack3_prak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack3_prak.ui.theme.Jetpack3prakTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Jetpack3prakTheme {
                VotingPage()
            }
        }
    }
}

@Composable
fun VotingPage() {
    var selectedId by remember { mutableStateOf<String?>(null) }
    var sisaVote by remember { mutableStateOf(1) }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(top = 97.dp),

        ) {
        Text(
            text = "Voting Mawapres 2025",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 55.dp)
        )
        Text(
            text = "Pilih Jagoanmu untuk Maju!",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 55.dp).padding(bottom = 46.dp)
        )

        CandidateCard("Andi", "235150000001", selectedId) { selectedId = it }
        CandidateCard("Budi", "235150000002", selectedId) { selectedId = it }
        CandidateCard("Cindi", "235150000003", selectedId) { selectedId = it }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (sisaVote > 0 && selectedId != null) {
                    sisaVote -= 1
                }
            },
            enabled = (sisaVote > 0 && selectedId != null),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 55.dp)
        ) {
            Text(text = "Pilih", fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sisa Vote: $sisaVote",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .padding(horizontal = 55.dp)
        )
    }
}


@Composable
fun CandidateCard(
    name: String,
    id: String,
    selectedId: String?,
    onClick: (String) -> Unit
) {
    val isSelected = selectedId == id
    val backgroundColor = if (isSelected) Color(0xFF8A2BE2) else Color.White
    val textColor = if (isSelected) Color.White else Color.Black
    val borderColor = if (isSelected) Color(0xFF8A2BE2) else Color.Black

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp).padding(horizontal = 55.dp)
            .clickable { onClick(id) }
            .background(backgroundColor, shape = RoundedCornerShape(12.dp))
            .border(BorderStroke(2.dp, borderColor), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(text = name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor)
        Text(text = id, fontSize = 14.sp, color = textColor)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVotingPage() {
    Jetpack3prakTheme {
        VotingPage()
    }
}