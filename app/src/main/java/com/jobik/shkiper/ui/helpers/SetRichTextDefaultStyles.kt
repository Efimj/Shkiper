package com.jobik.shkiper.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.style.TextDecoration
import com.jobik.shkiper.ui.theme.AppTheme
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
@OptIn(ExperimentalRichTextApi::class)
fun SetRichTextDefaultStyles(
    richTextState: RichTextState,
) {
    val codeColor = AppTheme.colors.onPrimary
    val codeBackgroundColor = AppTheme.colors.primary.copy(alpha = .2f)
    val codeStrokeColor = AppTheme.colors.primary
    val linkColor = AppTheme.colors.text

    LaunchedEffect(Unit) {
        richTextState.setConfig(
            linkColor = linkColor,
            linkTextDecoration = TextDecoration.Underline,
            codeColor = codeColor,
            codeBackgroundColor = codeBackgroundColor,
            codeStrokeColor = codeStrokeColor
        )
    }
}