package com.example.jetpack3_prak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.unit.dp

import com.example.jetpack3_prak.ui.theme.Jetpack3prakTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Jetpack3prakTheme {

            }
        }
    }
}

@Composable
fun VotingPage() {
    var selectedId by remember { mutableStateOf<String?>(null) }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Voting Mawapres 2025",
            fontSize = 25.dp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 55.dp)
        )
        Text(
            text = "Voting Mawapres 2025",
            fontSize = 25.dp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 55.dp)
        )
    }
}