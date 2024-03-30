package com.jobik.shkiper.ui.modifiers

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll

fun Modifier.scrollConnectionToProvideVisibility(visible: MutableState<Boolean>) = composed {
    this.nestedScroll(
        remember {
            object : NestedScrollConnection {
                override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                    if (consumed.y < -30) {
                        visible.value = false
                    }
                    if (consumed.y > 30) {
                        visible.value = true
                    }
                    if (available.y > 0) {
                        visible.value = true
                    }

                    return super.onPostScroll(consumed, available, source)
                }
            }
        }
    )
}