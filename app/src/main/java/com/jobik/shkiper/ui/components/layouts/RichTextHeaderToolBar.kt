package com.jobik.shkiper.ui.components.layouts

import android.util.Log
import androidx.annotation.Keep
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.RichTextStyleButton
import com.jobik.shkiper.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.topWindowInsetsPadding
import com.jobik.shkiper.ui.theme.CustomTheme
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun RichTextHeaderToolBar(
    modifier: Modifier = Modifier,
    state: RichTextState,
) {
    var isTextColorFillEnabled by rememberSaveable { mutableStateOf(false) }
    var isTextBackgroundFillEnabled by rememberSaveable { mutableStateOf(false) }
    val activeColor = CustomTheme.colors.primary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .horizontalWindowInsetsPadding()
            .topWindowInsetsPadding()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 12.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RichTextStyleButton(
            isActive = isTextColorFillEnabled,
            onClick = {
                tryCatchWrapper {
                    isTextColorFillEnabled = !isTextColorFillEnabled
                    state.toggleSpanStyle(SpanStyle(color = activeColor))
                }
            },
            icon = R.drawable.format_color_text_fill0_wght400_grad0_opsz24,
            contentDescription = ""
        )
        RichTextStyleButton(
            isActive = isTextBackgroundFillEnabled,
            onClick = {
                tryCatchWrapper {
                    isTextBackgroundFillEnabled = !isTextBackgroundFillEnabled
                    state.toggleSpanStyle(SpanStyle(background = activeColor))
                }
            },
            icon = R.drawable.format_color_fill_fill0_wght400_grad0_opsz24,
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
                isActive = state.isCodeSpan,
                onClick = { tryCatchWrapper { state.toggleCodeSpan() } },
                icon = R.drawable.terminal_fill0_wght400_grad0_opsz24,
                contentDescription = ""
            )
            Divider(
                modifier = Modifier
                    .heightIn(30.dp)
                    .width(1.dp),
                color = CustomTheme.colors.textSecondary.copy(alpha = .2f)
            )
            RichTextStyleButton(
                isActive = state.currentParagraphStyle.textAlign == TextAlign.Left,
                onClick = { tryCatchWrapper { state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Left)) } },
                icon = R.drawable.format_align_left_fill0_wght400_grad0_opsz24,
                contentDescription = ""
            )
        }
        RichTextStyleButton(
            isActive = state.currentParagraphStyle.textAlign == TextAlign.Center,
            onClick = { tryCatchWrapper { state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center)) } },
            icon = R.drawable.format_align_center_fill0_wght400_grad0_opsz24,
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
                isActive = state.currentParagraphStyle.textAlign == TextAlign.Right,
                onClick = { tryCatchWrapper { state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Right)) } },
                icon = R.drawable.format_align_right_fill0_wght400_grad0_opsz24,
                contentDescription = ""
            )
            Divider(
                modifier = Modifier
                    .heightIn(30.dp)
                    .width(1.dp),
                color = CustomTheme.colors.textSecondary.copy(alpha = .2f)
            )
            RichTextStyleButton(
                isActive = state.isUnorderedList,
                onClick = { tryCatchWrapper { state.toggleUnorderedList() } },
                icon = R.drawable.format_list_bulleted_fill0_wght400_grad0_opsz24,
                contentDescription = ""
            )
        }
        RichTextStyleButton(
            isActive = state.isOrderedList,
            onClick = { tryCatchWrapper { state.toggleOrderedList() } },
            icon = R.drawable.format_list_numbered_fill0_wght400_grad0_opsz24,
            contentDescription = ""
        )

    }
}

@Keep
private fun tryCatchWrapper(function: () -> Unit) {
    try {
        function()
    } catch (e: Exception) {
        Log.i("RichTextHeaderToolBar", e.message.toString())
    }
}