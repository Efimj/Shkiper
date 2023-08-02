package com.jobik.shkiper.ui.modifiers

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

private enum class ItemState { Pressed, Idle }

fun Modifier.bounceClick(
    targetValue: Float = 0.90f
) = composed {
    var itemState by remember { mutableStateOf(ItemState.Idle) }
    val scale by animateFloatAsState(if (itemState == ItemState.Pressed) targetValue else 1f)

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { }
        )
        .pointerInput(itemState) {
            awaitPointerEventScope {
                itemState = if (itemState == ItemState.Pressed) {
                    waitForUpOrCancellation()
                    ItemState.Idle
                } else {
                    awaitFirstDown(false)
                    ItemState.Pressed
                }
            }
        }
}