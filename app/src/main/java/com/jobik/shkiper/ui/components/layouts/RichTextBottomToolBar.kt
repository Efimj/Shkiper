package com.jobik.shkiper.ui.components.layouts

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.RichTextStyleButton
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun RichTextBottomToolBar(
    modifier: Modifier = Modifier,
    state: RichTextState,
    onClose: () -> Unit
) {
    val MainHeaderSize = 22.sp
    val SecondaryHeaderSize = 19.sp
    val PlainTextSize = 16.sp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .height(56.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 10.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RichTextStyleButton(
            isActive = state.currentSpanStyle.fontSize == MainHeaderSize,
            onClick = { state.toggleSpanStyle(SpanStyle(fontSize = if (state.currentSpanStyle.fontSize == MainHeaderSize) PlainTextSize else MainHeaderSize)) },
            icon = R.drawable.format_h1_fill0_wght400_grad0_opsz24,
            contentDescription = ""
        )
        RichTextStyleButton(
            isActive = state.currentSpanStyle.fontSize == SecondaryHeaderSize,
            onClick = { state.toggleSpanStyle(SpanStyle(fontSize = if (state.currentSpanStyle.fontSize == SecondaryHeaderSize) PlainTextSize else SecondaryHeaderSize)) },
            icon = R.drawable.format_h2_fill0_wght400_grad0_opsz24,
            contentDescription = ""
        )
        RichTextStyleButton(
            isActive = state.currentSpanStyle.fontSize == PlainTextSize || state.currentSpanStyle.fontSize == TextUnit.Unspecified,
            onClick = { state.toggleSpanStyle(SpanStyle(fontSize = PlainTextSize)) },
            icon = R.drawable.match_case_fill0_wght400_grad0_opsz24,
            contentDescription = ""
        )
        RichTextStyleButton(
            isActive = state.currentSpanStyle.fontWeight == FontWeight.Bold,
            onClick = { state.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold)) },
            icon = R.drawable.format_bold_fill0_wght400_grad0_opsz24,
            contentDescription = ""
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            RichTextStyleButton(
                isActive = false,
                onClick = onClose,
                icon = R.drawable.close_fill0_wght400_grad0_opsz24,
                contentDescription = ""
            )
        }

    }
}
