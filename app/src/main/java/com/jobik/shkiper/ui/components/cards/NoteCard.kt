package com.jobik.shkiper.ui.components.cards

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.Reminder
import com.jobik.shkiper.database.models.RepeatMode
import com.jobik.shkiper.helpers.DateHelper
import com.jobik.shkiper.helpers.TextHelper.Companion.removeMarkdownStyles
import com.jobik.shkiper.ui.helpers.MultipleEventsCutter
import com.jobik.shkiper.ui.helpers.SetRichTextDefaultStyles
import com.jobik.shkiper.ui.helpers.get
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.AppTheme
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    header: String? = null,
    text: String? = null,
    reminder: Reminder? = null,
    markedText: String? = null,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val headerStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
    val bodyStyle = MaterialTheme.typography.bodyMedium
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }
    val borderColor: Color by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.primary else Color.Transparent, label = "borderColor",
    )

    val bodyRichTextState = rememberRichTextState()
    SetRichTextDefaultStyles(bodyRichTextState)

    LaunchedEffect(text) {
        if (text !== null && bodyRichTextState.annotatedString.text !== text)
            bodyRichTextState.setHtml(text)
        else
            bodyRichTextState.setText("")
    }

    Card(
        modifier = modifier
            .bounceClick()
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .combinedClickable(
                onClick = { multipleEventsCutter.processEvent { onClick() } },
                onLongClick = onLongClick,
            ),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(2.dp, borderColor),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.container,
            contentColor = AppTheme.colors.text
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (markedText.isNullOrBlank())
                NoteContent(header, bodyRichTextState, headerStyle, bodyStyle)
            else
                NoteAnnotatedContent(header, bodyRichTextState, markedText, headerStyle, bodyStyle)
            if (header.isNullOrBlank() && removeMarkdownStyles(bodyRichTextState.toMarkdown()).isBlank()) {
                EmptyNoteContent(bodyStyle)
            }
            ReminderInformation(reminder)
        }
    }
}

@Composable
private fun ColumnScope.EmptyNoteContent(bodyStyle: TextStyle) {
    Text(
        text = stringResource(R.string.EmptyNote),
        maxLines = 10,
        overflow = TextOverflow.Ellipsis,
        style = bodyStyle,
        color = AppTheme.colors.textSecondary,
        modifier = Modifier.Companion.align(Alignment.CenterHorizontally)
    )
}

private fun getNextReminderDate(reminder: Reminder?): LocalDateTime {
    if (reminder == null) return LocalDateTime.now()
    return DateHelper.nextDateWithRepeating(
        notificationDate = LocalDateTime.of(reminder.date, reminder.time),
        repeatMode = reminder.repeat
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ReminderInformation(reminder: Reminder?) {
    val nextReminderDate = getNextReminderDate(reminder)
    val isDateFuture = DateHelper.isFutureDateTime(nextReminderDate)
    val fontSize = 13.sp

    if (reminder != null) {
        Spacer(modifier = Modifier.height(4.dp))
        val shape = RoundedCornerShape(5.dp)
        Row(
            modifier = Modifier
                .clip(shape)
                .basicMarquee(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                tint = AppTheme.colors.textSecondary,
                imageVector = if (reminder.repeat == RepeatMode.NONE) Icons.Default.Event else Icons.Default.Repeat,
                contentDescription = stringResource(R.string.Event),
                modifier = Modifier.height(15.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                DateHelper.getLocalizedDate(nextReminderDate.toLocalDate()),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = fontSize,
                    textDecoration = if (isDateFuture) TextDecoration.None else TextDecoration.LineThrough
                ),
                color = AppTheme.colors.textSecondary,
            )
            Spacer(Modifier.width(4.dp))
            if (isDateFuture)
                Text(
                    nextReminderDate.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = fontSize),
                    color = AppTheme.colors.textSecondary,
                )
        }
    }
}

@Composable
private fun NoteContent(header: String?, text: RichTextState, headerStyle: TextStyle, bodyStyle: TextStyle) {
    var headerLineCount by remember { mutableStateOf(1) }
    val maxBodyLines = 8
    val isBodyEmpty = removeMarkdownStyles(text.toMarkdown()).isBlank()

    if (!header.isNullOrBlank()) {
        Text(
            text = header,
            style = headerStyle,
            overflow = TextOverflow.Ellipsis,
            color = AppTheme.colors.onSecondaryContainer,
            onTextLayout = { textLayoutResult: TextLayoutResult ->
                headerLineCount = textLayoutResult.lineCount
            },
            maxLines = 3
        )
    }
    if (!isBodyEmpty && !header.isNullOrBlank())
        Spacer(modifier = Modifier.height(4.dp))
    if (!isBodyEmpty) {
        Text(
            text = removeMarkdownStyles(text.toMarkdown()),
            maxLines = maxBodyLines - headerLineCount,
            overflow = TextOverflow.Ellipsis,
            style = bodyStyle,
            color = AppTheme.colors.textSecondary,
        )
//        RichText(
//            state = richTextState,
//            maxLines = maxBodyLines - headerLineCount,
//            overflow = TextOverflow.Ellipsis,
//            style = bodyStyle,
//            color = CustomTheme.colors.textSecondary,
//        )
    }
}

@Composable
private fun NoteAnnotatedContent(
    header: String?,
    text: RichTextState,
    markedText: String,
    headerStyle: TextStyle,
    bodyStyle: TextStyle
) {
    var headerLineCount by remember { mutableStateOf(1) }
    val maxBodyLines = 8
    val isBodyEmpty = removeMarkdownStyles(text.toMarkdown()).isBlank()

    if (!header.isNullOrBlank()) {
        Text(
            text = buildAnnotatedString(
                text = header,
                substring = markedText,
                color = AppTheme.colors.primary,
                background = Color.Transparent
            ),
            style = headerStyle,
            overflow = TextOverflow.Ellipsis,
            color = AppTheme.colors.text,
            onTextLayout = { textLayoutResult: TextLayoutResult ->
                headerLineCount = textLayoutResult.lineCount
            },
            maxLines = 3,
        )
    }
    if (!isBodyEmpty && !header.isNullOrBlank())
        Spacer(modifier = Modifier.height(4.dp))
    if (!isBodyEmpty) {
        Text(
            text = buildAnnotatedString(
                text = removeMarkdownStyles(text.toMarkdown()),
                substring = markedText,
                color = AppTheme.colors.primary,
                background = Color.Transparent
            ),
            style = bodyStyle,
            overflow = TextOverflow.Ellipsis,
            color = AppTheme.colors.textSecondary,
            maxLines = maxBodyLines - headerLineCount,
        )
    }
}

fun buildAnnotatedString(text: String, substring: String, color: Color, background: Color): AnnotatedString {
    return buildAnnotatedString {
        append(text)
        var lastIndex = 0
        while (lastIndex != -1) {
            lastIndex = text.indexOf(substring, lastIndex, true)
            if (lastIndex == -1) break
            addStyle(
                style = SpanStyle(color = color, background = background),
                start = lastIndex,
                end = lastIndex + substring.length
            )
            lastIndex += substring.length
        }
    }
}