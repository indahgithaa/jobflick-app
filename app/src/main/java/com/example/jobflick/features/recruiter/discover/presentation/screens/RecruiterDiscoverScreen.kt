package com.example.jobflick.features.recruiter.discover.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.jobflick.core.ui.components.RecruiterBottomNavBar

@Composable
fun RecruiterDiscoverScreen(
    currentRoute: String,
    onTabSelected: (String) -> Unit
) {
    Scaffold(
        bottomBar = {
            RecruiterBottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = onTabSelected
            )
        }
    ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Discover Rekruter (kosong sementara)")
        }
    }
}
