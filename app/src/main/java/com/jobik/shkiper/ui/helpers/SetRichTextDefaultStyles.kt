package com.jobik.shkiper.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.style.TextDecoration
import com.jobik.shkiper.ui.theme.CustomTheme
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
@OptIn(ExperimentalRichTextApi::class)
fun SetRichTextDefaultStyles(
    richTextState: RichTextState,
) {
    val codeColor = CustomTheme.colors.textOnActive
    val codeBackgroundColor = CustomTheme.colors.active.copy(alpha = .2f)
    val codeStrokeColor = CustomTheme.colors.active
    val linkColor = CustomTheme.colors.text

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