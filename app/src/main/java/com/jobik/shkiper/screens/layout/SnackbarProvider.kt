package com.jobik.shkiper.screens.layout

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.screens.layout.NavigationBar.AppNavigationBarState
import com.jobik.shkiper.screens.layout.NavigationBar.DefaultNavigationValues
import com.jobik.shkiper.ui.components.cards.SnackbarCard
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom

@Composable
fun BoxScope.SnackbarProvider() {
    val bottomPadding = 20.dp
    val currentOffsetY =
        (if (AppNavigationBarState.isVisible.value) bottomPadding + DefaultNavigationValues().containerHeight else 0.dp)
    val currentOffsetYValue = animateDpAsState(targetValue = currentOffsetY, label = "currentOffsetYValue")

    SnackbarHost(
        hostState = SnackbarHostUtil.snackbarHostState,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom))
    ) { snackbarData ->
        val customVisuals = snackbarData.visuals as SnackbarVisualsCustom
        Box(
            modifier = Modifier
                .offset(y = -currentOffsetYValue.value)
                .align(Alignment.BottomCenter)
        ) {
            SnackbarCard(customVisuals)
        }
    }
}