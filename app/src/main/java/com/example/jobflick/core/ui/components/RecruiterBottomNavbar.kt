package com.example.jobflick.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobflick.R
import com.example.jobflick.core.ui.theme.GrayInactive
import com.example.jobflick.core.ui.theme.OrangePrimary
import com.example.jobflick.navigation.Routes

data class RecruiterBottomNavItem(
    val route: String,
    val label: String,
    val activeIconRes: Int,
    val inactiveIconRes: Int,
)

private val recruiterBottomNavItems = listOf(
    RecruiterBottomNavItem(
        route = Routes.RECRUITER_DASHBOARD,
        label = "Dashboard",
        activeIconRes = R.drawable.dashboard_recruiter_active,
        inactiveIconRes = R.drawable.dashboard_recruiter_inactive
    ),
    RecruiterBottomNavItem(
        route = Routes.RECRUITER_DISCOVER,
        label = "Discover",
        activeIconRes = R.drawable.discover_recruiter_active,
        inactiveIconRes = R.drawable.discover_inactive_navbar,
    ),
    RecruiterBottomNavItem(
        route = Routes.RECRUITER_PROFILE,
        label = "Profile",
        activeIconRes = R.drawable.profile_recruiter_active,
        inactiveIconRes = R.drawable.profile_inactive_navbar,
    ),
)

@Composable
fun RecruiterBottomNavBar(
    currentRoute: String?,
    onItemSelected: (String) -> Unit,
) {
    Surface(
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            recruiterBottomNavItems.forEach { item ->
                val isSelected = currentRoute == item.route

                Column(
                    modifier = Modifier
                        .clickable { onItemSelected(item.route) }
                        .padding(vertical = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(
                            id = if (isSelected) item.activeIconRes else item.inactiveIconRes
                        ),
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) OrangePrimary else GrayInactive
                    )
                }
            }
        }
    }
}
