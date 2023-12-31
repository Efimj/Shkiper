package com.jobik.shkiper.ui.components.layouts

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.RichTextStyleButton
import com.jobik.shkiper.ui.theme.CustomTheme
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun RichTextHeaderToolBar(
    modifier: Modifier = Modifier,
    state: RichTextState,
) {
    var isTextColorFillEnabled by rememberSaveable { mutableStateOf(false) }
    var isTextBackgroundFillEnabled by rememberSaveable { mutableStateOf(false) }
    val activeColor = CustomTheme.colors.active

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
            isActive = isTextColorFillEnabled,
            onClick = {
                isTextColorFillEnabled = !isTextColorFillEnabled
                state.toggleSpanStyle(SpanStyle(color = activeColor))
            },
            icon = R.drawable.format_color_text_fill0_wght400_grad0_opsz24,
            contentDescription = ""
        )
        RichTextStyleButton(
            isActive = isTextBackgroundFillEnabled,
            onClick = {
                isTextBackgroundFillEnabled = !isTextBackgroundFillEnabled
                state.toggleSpanStyle(SpanStyle(background = activeColor))
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
                onClick = { state.toggleCodeSpan() },
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
                onClick = { state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Left)) },
                icon = R.drawable.format_align_left_fill0_wght400_grad0_opsz24,
                contentDescription = ""
            )
        }
        RichTextStyleButton(
            isActive = state.currentParagraphStyle.textAlign == TextAlign.Center,
            onClick = { state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center)) },
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
                onClick = { state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Right)) },
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
                onClick = { state.toggleUnorderedList() },
                icon = R.drawable.format_list_bulleted_fill0_wght400_grad0_opsz24,
                contentDescription = ""
            )
        }
        RichTextStyleButton(
            isActive = state.isOrderedList,
            onClick = { state.toggleOrderedList() },
            icon = R.drawable.format_list_numbered_fill0_wght400_grad0_opsz24,
            contentDescription = ""
        )

    }
}
