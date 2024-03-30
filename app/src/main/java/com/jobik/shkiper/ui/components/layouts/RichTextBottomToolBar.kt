package com.jobik.shkiper.ui.components.layouts

import android.util.Log
import androidx.annotation.Keep
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Divider
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.RichTextStyleButton
import com.jobik.shkiper.ui.components.modals.InsertLinkDialog
import com.jobik.shkiper.ui.helpers.bottomWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.shkiper.ui.theme.CustomTheme
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun RichTextBottomToolBar(
    modifier: Modifier = Modifier,
    state: RichTextState,
    onClose: () -> Unit
) {
    var isInsertLinkDialogOpen by remember { mutableStateOf(false) }
    val MainHeaderSize = 22.sp
    val SecondaryHeaderSize = 19.sp
    val PlainTextSize = 16.sp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .bottomWindowInsetsPadding()
            .horizontalWindowInsetsPadding()
            .padding(horizontal = 12.dp)
            .height(56.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 12.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RichTextStyleButton(
            isActive = state.currentSpanStyle.fontSize == MainHeaderSize,
            onClick = { tryCatchWrapper { state.toggleSpanStyle(SpanStyle(fontSize = if (state.currentSpanStyle.fontSize == MainHeaderSize) PlainTextSize else MainHeaderSize)) } },
            icon = R.drawable.format_h1_fill0_wght400_grad0_opsz24,
            contentDescription = ""
        )
        RichTextStyleButton(
            isActive = state.currentSpanStyle.fontSize == SecondaryHeaderSize,
            onClick = { tryCatchWrapper { state.toggleSpanStyle(SpanStyle(fontSize = if (state.currentSpanStyle.fontSize == SecondaryHeaderSize) PlainTextSize else SecondaryHeaderSize)) } },
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
                isActive = state.currentSpanStyle.fontSize == PlainTextSize || state.currentSpanStyle.fontSize == TextUnit.Unspecified,
                onClick = { tryCatchWrapper { state.toggleSpanStyle(SpanStyle(fontSize = PlainTextSize)) } },
                icon = R.drawable.match_case_fill0_wght400_grad0_opsz24,
                contentDescription = ""
            )
            Divider(
                modifier = Modifier
                    .heightIn(30.dp)
                    .width(1.dp),
                color = CustomTheme.colors.textSecondary.copy(alpha = .2f)
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
            Divider(
                modifier = Modifier
                    .heightIn(30.dp)
                    .width(1.dp),
                color = CustomTheme.colors.textSecondary.copy(alpha = .2f)
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
            onInsert = { text, url -> state.addLink(text, url); isInsertLinkDialogOpen = !isInsertLinkDialogOpen },
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