package com.example.jobflick.features.jobseeker.discover.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Container kartu dengan gesture swipe:
 * - Kanan  -> onSwipedRight
 * - Kiri   -> onSwipedLeft
 * - Atas   -> onSwipedUp
 * onDrag dipakai untuk update progress swipe (misal buat efek tint).
 */
@Composable
fun SwipeableCard(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onDrag: (x: Float, y: Float) -> Unit = { _, _ -> },
    onSwipedLeft: () -> Unit = {},
    onSwipedRight: () -> Unit = {},
    onSwipedUp: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val x = remember { Animatable(0f) }
    val y = remember { Animatable(0f) }
    val rot = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Card(
        modifier = modifier
            .then(
                if (enabled) {
                    Modifier.pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = {
                                val thresholdX = 200f
                                val thresholdY = 220f

                                when {
                                    x.value > thresholdX  -> onSwipedRight()
                                    x.value < -thresholdX -> onSwipedLeft()
                                    y.value < -thresholdY -> onSwipedUp()
                                    else -> {
                                        scope.launch {
                                            x.animateTo(0f, spring(stiffness = Spring.StiffnessMedium))
                                        }
                                        scope.launch {
                                            y.animateTo(0f, spring(stiffness = Spring.StiffnessMedium))
                                        }
                                        scope.launch {
                                            rot.animateTo(0f, spring(stiffness = Spring.StiffnessMedium))
                                        }
                                    }
                                }
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            val (dx, dy) = dragAmount
                            scope.launch {
                                x.snapTo(x.value + dx)
                                y.snapTo(y.value + dy)
                                rot.snapTo((x.value / 20f).coerceIn(-20f, 20f))
                            }
                            onDrag(x.value, y.value)
                        }
                    }
                } else Modifier
            )
            .rotate(rot.value)
            .offset { IntOffset(x.value.roundToInt(), y.value.roundToInt()) },
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        content()
    }
}
