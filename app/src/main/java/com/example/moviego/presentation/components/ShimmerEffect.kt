package com.example.moviego.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
fun Modifier.shimmerEffect(
    shimmerWidth: Dp = 200.dp,
    animationDuration: Int = 1000,
    delayBetweenAnimations: Int = 300
): Modifier = composed {


    // Define colors based on theme
    val shimmerColors = listOf(
        Color(0xFF2C2C2C),    // Dark base
        Color(0xFF404040),    // Lighter shimmer
        Color(0xFF2C2C2C)     // Back to dark
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration,
                delayMillis = delayBetweenAnimations,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )
    val shimmerWidthPx = with(LocalDensity.current) { shimmerWidth.toPx() }

    this.background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnimation.value - shimmerWidthPx, 0f),
            end = Offset(translateAnimation.value, 0f)
        )
    )
}
