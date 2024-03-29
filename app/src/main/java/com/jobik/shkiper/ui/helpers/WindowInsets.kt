package com.jobik.shkiper.ui.helpers

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp

fun Modifier.topWindowInsetsPadding() = composed {
    this.padding(top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding())
}

fun Modifier.bottomWindowInsetsPadding() = composed {
    this.padding(bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding())
}

fun Modifier.startWindowInsetsPadding() = composed {
    val layoutDirection = LocalLayoutDirection.current
    this.padding(start = WindowInsets.safeDrawing.asPaddingValues().calculateLeftPadding(layoutDirection))
}

fun Modifier.endWindowInsetsPadding() = composed {
    val layoutDirection = LocalLayoutDirection.current
    this.padding(end = WindowInsets.safeDrawing.asPaddingValues().calculateRightPadding(layoutDirection))
}

fun Modifier.verticalWindowInsetsPadding() = composed {
    this
        .padding(
            top = WindowInsets.safeDrawing
                .asPaddingValues()
                .calculateTopPadding()
        )
        .padding(
            bottom = WindowInsets.safeDrawing
                .asPaddingValues()
                .calculateBottomPadding()
        )
}

fun Modifier.horizontalWindowInsetsPadding() = composed {
    val layoutDirection = LocalLayoutDirection.current
    this
        .padding(
            start = WindowInsets.safeDrawing
                .asPaddingValues()
                .calculateLeftPadding(layoutDirection)
        )
        .padding(
            end = WindowInsets.safeDrawing
                .asPaddingValues()
                .calculateRightPadding(layoutDirection)
        )
}

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.allWindowInsetsPadding() = composed {
    val layoutDirection = LocalLayoutDirection.current
    this
        .padding(
            top = WindowInsets.safeDrawing
                .asPaddingValues()
                .calculateTopPadding()
        )
        .padding(
            bottom = WindowInsets.safeDrawing
                .asPaddingValues()
                .calculateBottomPadding()
        )
        .padding(
            start = WindowInsets.safeDrawing
                .asPaddingValues()
                .calculateLeftPadding(layoutDirection)
        )
        .padding(
            end = WindowInsets.safeDrawing
                .asPaddingValues()
                .calculateRightPadding(layoutDirection)
        )
}

@Composable
fun topWindowInsetsPadding(): Dp {
    return WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
}

@Composable
fun bottomWindowInsetsPadding(): Dp {
    return WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
}

@Composable
fun TopWindowInsetsSpacer() =
    Spacer(
        Modifier.windowInsetsTopHeight(
            WindowInsets.safeDrawing
        )
    )

@Composable
fun BottomWindowInsetsSpacer() =
    Spacer(
        Modifier.windowInsetsBottomHeight(
            WindowInsets.safeDrawing
        )
    )