package com.jobik.shkiper.ui.components.layouts

import android.util.Log
import androidx.annotation.Keep
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.RichTextStyleButton
import com.jobik.shkiper.ui.components.modals.InsertLinkDialog
import com.jobik.shkiper.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.shkiper.ui.theme.AppTheme
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun RichTextBottomToolBar(
    modifier: Modifier = Modifier,
    state: RichTextState,
    onClose: () -> Unit
) {
    var isInsertLinkDialogOpen by remember { mutableStateOf(false) }
    val TextSize = MaterialTheme.typography.bodyMedium.fontSize
    val TextLargeSize = TextSize * 1.4f
    val TextExtraLargeSize = TextLargeSize * 1.4f

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .horizontalWindowInsetsPadding()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 12.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RichTextStyleButton(
            isActive = state.currentSpanStyle.fontSize == TextExtraLargeSize,
            onClick = { tryCatchWrapper { state.toggleSpanStyle(SpanStyle(fontSize = if (state.currentSpanStyle.fontSize == TextExtraLargeSize) TextSize else TextExtraLargeSize)) } },
            icon = R.drawable.format_h1_fill0_wght400_grad0_opsz24,
            contentDescription = ""
        )
        RichTextStyleButton(
            isActive = state.currentSpanStyle.fontSize == TextLargeSize,
            onClick = { tryCatchWrapper { state.toggleSpanStyle(SpanStyle(fontSize = if (state.currentSpanStyle.fontSize == TextLargeSize) TextSize else TextLargeSize)) } },
            icon = R.drawable.format_h2_fill0_wght400_grad0_opsz24,
            contentDescription = ""
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = 6.dp,
                alignment = Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RichTextStyleButton(
                isActive = state.currentSpanStyle.fontSize == TextSize || state.currentSpanStyle.fontSize == TextUnit.Unspecified,
                onClick = { tryCatchWrapper { state.toggleSpanStyle(SpanStyle(fontSize = TextSize)) } },
                icon = R.drawable.match_case_fill0_wght400_grad0_opsz24,
                contentDescription = ""
            )
            VerticalDivider(
                modifier = Modifier
                    .heightIn(max = 30.dp)
                    .width(1.dp),
                color = AppTheme.colors.textSecondary.copy(alpha = .2f)
            )
            RichTextStyleButton(
                isActive = state.currentSpanStyle.fontWeight == FontWeight.Bold,
                onClick = { tryCatchWrapper { state.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold)) } },
                icon = R.drawable.format_bold_fill0_wght400_grad0_opsz24,
                contentDescription = ""
            )
        }
        RichTextStyleButton(
            isActive = state.currentSpanStyle.fontStyle == FontStyle.Italic,
            onClick = { tryCatchWrapper { state.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic)) } },
            icon = R.drawable.format_italic_fill0_wght400_grad0_opsz24,
            contentDescription = ""
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = 6.dp,
                alignment = Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RichTextStyleButton(
                isActive = state.currentSpanStyle.textDecoration == TextDecoration.LineThrough,
                onClick = { tryCatchWrapper { state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) } },
                icon = R.drawable.format_strikethrough_black_24dp,
                contentDescription = ""
            )
            VerticalDivider(
                modifier = Modifier
                    .heightIn(max = 30.dp)
                    .width(1.dp),
                color = AppTheme.colors.textSecondary.copy(alpha = .2f)
            )
            RichTextStyleButton(
                isActive = state.isLink,
                onClick = { tryCatchWrapper { isInsertLinkDialogOpen = !isInsertLinkDialogOpen } },
                icon = R.drawable.add_link_fill0_wght400_grad0_opsz24,
                contentDescription = ""
            )
        }
        RichTextStyleButton(
            isActive = false,
            onClick = {
                tryCatchWrapper {
                    state.removeParagraphStyle(state.currentParagraphStyle)
                    state.removeSpanStyle(state.currentSpanStyle)
                    state.removeCodeSpan()
                }
            },
            icon = R.drawable.format_clear_fill0_wght400_grad0_opsz24,
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
    if (isInsertLinkDialogOpen)
        InsertLinkDialog(
            onInsert = { text, url ->
                state.addLink(text, url); isInsertLinkDialogOpen = !isInsertLinkDialogOpen
            },
            onGoBack = { isInsertLinkDialogOpen = !isInsertLinkDialogOpen })
}

@Keep
private fun tryCatchWrapper(function: () -> Unit) {
    try {
        function()
    } catch (e: Exception) {
        Log.i("RichTextBottomToolBar", e.message.toString())
    }
}