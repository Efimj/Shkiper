package com.jobik.shkiper.ui.modifiers

import androidx.compose.animation.core.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.circularRotation() = composed {
    val rotation = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        with(rotation) {
            animateTo(
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    this
        .rotate(rotation.value)
}