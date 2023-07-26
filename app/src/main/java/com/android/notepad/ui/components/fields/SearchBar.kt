package com.android.notepad.ui.components.fields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun SearchBar(
    searchBarHeight: Dp,
    searchBarOffsetHeightPx: Float,
    isVisible: Boolean,
    value: String,
    onChange: (newValue: String) -> Unit
) {
    val searchBarFloatHeight = with(LocalDensity.current) { searchBarHeight.roundToPx().toFloat() }

    AnimatedVisibility(
        isVisible,
        enter = slideIn(tween(200, easing = LinearOutSlowInEasing)) {
            IntOffset(0, -searchBarFloatHeight.roundToInt())
        },
        exit = slideOut(tween(200, easing = FastOutSlowInEasing)) {
            IntOffset(0, -searchBarFloatHeight.roundToInt())
        },
    ) {
        Box(
            modifier = Modifier.height(searchBarHeight).padding(20.dp, 10.dp, 20.dp, 0.dp)
                .offset { IntOffset(x = 0, y = searchBarOffsetHeightPx.roundToInt()) },
        ) {
            SearchField(
                search = value,
                onTrailingIconClick = { onChange("") },
                onValueChange = onChange
            )
        }
    }
}