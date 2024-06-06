package com.jobik.shkiper.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import com.jobik.shkiper.ui.theme.AppTheme
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.RichTextConfig
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun SetRichTextDefaultStyles(
    richTextState: RichTextState,
) {
    val codeColor = AppTheme.colors.onSecondaryContainer
    val codeStrokeColor = AppTheme.colors.secondaryContainer
    val linkColor = AppTheme.colors.text

    LaunchedEffect(Unit) {
        richTextState.config.linkColor = linkColor
        richTextState.config.linkTextDecoration = TextDecoration.Underline
        richTextState.config.codeSpanColor = codeColor
        richTextState.config.codeSpanBackgroundColor = Color.Transparent
        richTextState.config.codeSpanStrokeColor = codeStrokeColor
    }
}