package com.jobik.shkiper.screens.layout

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.screens.layout.navigation.AppNavigationBarState
import com.jobik.shkiper.screens.layout.navigation.DefaultNavigationValues
import com.jobik.shkiper.ui.components.cards.SnackbarCard
import com.jobik.shkiper.ui.helpers.px
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom

@Composable
fun BoxScope.SnackBarProvider() {
    val bottomPadding = 20.dp
    val currentOffsetY =
        (if (AppNavigationBarState.isVisible.value) bottomPadding + DefaultNavigationValues().containerHeight else 0.dp)
    val currentOffsetYValue =
        animateDpAsState(targetValue = currentOffsetY, label = "currentOffsetYValue")
    val pixelYOffset = -currentOffsetYValue.value.px

    SnackbarHost(
        hostState = SnackbarHostUtil.snackbarHostState,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom))
    ) { snackBarData ->
        val customVisuals = snackBarData.visuals as SnackbarVisualsCustom
        Box(
            modifier = Modifier
                .offset { IntOffset(x = 0, y = pixelYOffset) }
                .align(Alignment.BottomCenter),
        ) {
            SnackbarCard(customVisuals)
        }
    }
}