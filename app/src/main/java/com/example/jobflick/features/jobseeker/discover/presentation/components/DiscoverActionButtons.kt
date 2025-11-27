package com.example.jobflick.features.jobseeker.discover.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.jobflick.R
import com.example.jobflick.core.ui.theme.BlueSave
import com.example.jobflick.core.ui.theme.GreenApply
import com.example.jobflick.core.ui.theme.RedSkip
import com.example.jobflick.core.ui.theme.YellowUndo

@Composable
fun DiscoverActionButtons(
    modifier: Modifier = Modifier,
    onUndo: () -> Unit,
    onSkip: () -> Unit,
    onApplyClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ActionButton("Batal", YellowUndo, R.drawable.undo, onUndo)
        ActionButton("Lewati", RedSkip, R.drawable.close, onSkip)
        ActionButton("Lamar", GreenApply, R.drawable.check, onApplyClick)
        ActionButton("Simpan", BlueSave, R.drawable.bookmark, onSaveClick)
    }
}

@Composable
private fun ActionButton(
    label: String,
    tint: Color,
    iconRes: Int,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = Color.Transparent,
            border = BorderStroke(2.dp, tint),
            modifier = Modifier.size(56.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = label,
                    tint = tint,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}
