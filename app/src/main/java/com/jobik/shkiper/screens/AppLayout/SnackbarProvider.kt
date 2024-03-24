package com.jobik.shkiper.screens.AppLayout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jobik.shkiper.ui.components.cards.SnackbarCard
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom

@Composable
fun BoxScope.SnackbarProvider() {
    SnackbarHost(
        hostState = SnackbarHostUtil.snackbarHostState,
        modifier = Modifier.align(Alignment.BottomCenter)
    ) { snackbarData ->
        Box(
            Modifier
//                    .offset(y = -navigationContainerHeight)
                .align(Alignment.BottomCenter)
        ) {
            val customVisuals = snackbarData.visuals as SnackbarVisualsCustom
            SnackbarCard(customVisuals)
        }
    }
}