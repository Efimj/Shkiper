package com.jobik.shkiper.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import kotlin.math.roundToInt

inline val Int.dp: Dp
    @Composable get() = with(LocalDensity.current) { this@dp.toDp() }

inline val Dp.px: Int
    @Composable get() = with(LocalDensity.current) { this@px.toPx().roundToInt() }