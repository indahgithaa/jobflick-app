package com.example.jobflick.features.jobseeker.roadmap.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobflick.core.ui.components.JFSearchBar
import com.example.jobflick.core.ui.theme.Jost
import com.example.jobflick.features.jobseeker.roadmap.presentation.di.rememberRoadmapViewModel

@Composable
fun RoadmapScreen(
    onRoleSelected: (String) -> Unit
) {
    val viewModel = rememberRoadmapViewModel()
    val availableRoles = viewModel.availableRoles

    RoadmapRoleListScreen(
        availableRoles = availableRoles,
        onSelectRole = { roleName ->
            viewModel.selectRole(roleName)
            onRoleSelected(roleName)
        }
    )
}

@Composable
fun RoadmapRoleListScreen(
    availableRoles: List<String>,
    onSelectRole: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    Scaffold { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Roadmap",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = Jost,
                    fontWeight = FontWeight.Medium,
                    fontSize = 26.sp,
                )
            )
            Spacer(Modifier.height(16.dp))

            JFSearchBar(
                value = query,
                onValueChange = { query = it },
                placeholder = "Cari role yang kamu inginkan..."
            )

            Spacer(Modifier.height(24.dp))

            val filtered = if (query.isBlank()) {
                availableRoles
            } else {
                availableRoles.filter {
                    it.contains(query, ignoreCase = true)
                }
            }

            filtered.forEach { roleName ->
                Text(
                    text = roleName,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onSelectRole(roleName) }
                )
                Divider()
            }
        }
    }
}
