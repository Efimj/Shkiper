package com.example.notepadapp.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notepadapp.R

@Composable
fun SplashScreen() {
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) { onStartUp(animatedProgress) }
    val alpha = animateFloatAsState(
        if (animatedProgress.value > 0f) 1f else 0f,
        animationSpec = tween(durationMillis = 350)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo",
            modifier = Modifier
                .alpha(alpha.value)
                .fillMaxWidth()
                .height(250.dp)
        )
    }
}

private suspend fun onStartUp(
    animatedProgress: Animatable<Float, AnimationVector1D>,
) {
    animatedProgress.animateTo(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 350)
    )
}
