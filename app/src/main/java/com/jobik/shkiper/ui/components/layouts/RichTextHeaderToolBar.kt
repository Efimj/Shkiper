package com.jobik.shkiper.ui.components.layouts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.RichTextStyleButton
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun RichTextHeaderToolBar(
    modifier: Modifier = Modifier,
    state: RichTextState,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .height(56.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 12.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RichTextStyleButton(
            isActive = state.currentParagraphStyle.textAlign == TextAlign.Left,
            onClick = { state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Left)) },
            icon = R.drawable.format_align_left_fill0_wght400_grad0_opsz24,
            contentDescription = ""
        )
        RichTextStyleButton(
            isActive = state.currentParagraphStyle.textAlign == TextAlign.Center,
            onClick = { state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center)) },
            icon = R.drawable.format_align_center_fill0_wght400_grad0_opsz24,
            contentDescription = ""
        )
        RichTextStyleButton(
            isActive = state.currentParagraphStyle.textAlign == TextAlign.Right,
            onClick = { state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Right)) },
            icon = R.drawable.format_align_right_fill0_wght400_grad0_opsz24,
            contentDescription = ""
        )
    }
}
