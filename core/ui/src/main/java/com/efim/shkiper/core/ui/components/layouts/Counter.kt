package com.jobik.shkiper.ui.components.layouts

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.jobik.shkiper.ui.theme.AppTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Counter(count: Int, style: TextStyle = MaterialTheme.typography.body1, color: Color = AppTheme.colors.text) {
    Row(
        modifier = Modifier.animateContentSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        count.toString()
            .mapIndexed { index, c -> Digit(c, count, index) }
            .forEach { digit ->
                AnimatedContent(
                    targetState = digit,
                    transitionSpec = {
                        if (targetState > initialState) {
                            slideInVertically { -it } with slideOutVertically { it }
                        } else {
                            slideInVertically { it } with slideOutVertically { -it }
                        }
                    },
                    label = "Counter"
                ) { digit ->
                    Text(
                        "${digit.digitChar}",
                        style = style,
                        textAlign = TextAlign.Center,
                        color = color
                    )
                }
            }
    }

}

private data class Digit(val digitChar: Char, val fullNumber: Int, val place: Int) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Digit -> digitChar == other.digitChar
            else -> super.equals(other)
        }
    }
}

private operator fun Digit.compareTo(other: Digit): Int {
    return fullNumber.compareTo(other.fullNumber)
}