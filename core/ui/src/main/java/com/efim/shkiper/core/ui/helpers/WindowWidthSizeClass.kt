package com.jobik.shkiper.ui.helpers

import androidx.annotation.Keep
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Keep
enum class WindowWidthSizeClass(val minValue: Int) {
    Compact(0),
    Medium(600),
    Expanded(840),
}

@Keep
enum class WindowHeightSizeClass(val minValue: Int) {
    Compact(0),
    Medium(480),
    Expanded(900),
}

@Composable
fun isWidth(sizeClass: WindowWidthSizeClass): Boolean {
    val configuration = LocalConfiguration.current
    val widthInDp = configuration.screenWidthDp.dp

    val (mediumMin, expandedMin) = remember {
        Pair(WindowWidthSizeClass.Medium.minValue.dp, WindowWidthSizeClass.Expanded.minValue.dp)
    }

    return when (sizeClass) {
        WindowWidthSizeClass.Compact -> widthInDp < mediumMin
        WindowWidthSizeClass.Medium -> widthInDp >= WindowWidthSizeClass.Medium.minValue.dp && widthInDp < WindowWidthSizeClass.Expanded.minValue.dp
        WindowWidthSizeClass.Expanded -> widthInDp >= expandedMin
    }
}

@Composable
fun isHeight(sizeClass: WindowHeightSizeClass): Boolean {
    val configuration = LocalConfiguration.current
    val widthInDp = configuration.screenWidthDp.dp

    val (mediumMin, expandedMin) = remember {
        Pair(WindowHeightSizeClass.Medium.minValue.dp, WindowHeightSizeClass.Expanded.minValue.dp)
    }

    return when (sizeClass) {
        WindowHeightSizeClass.Compact -> widthInDp < mediumMin
        WindowHeightSizeClass.Medium -> widthInDp >= WindowHeightSizeClass.Medium.minValue.dp && widthInDp < WindowHeightSizeClass.Expanded.minValue.dp
        WindowHeightSizeClass.Expanded -> widthInDp >= expandedMin
    }
}