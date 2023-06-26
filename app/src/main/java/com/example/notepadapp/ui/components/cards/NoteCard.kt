package com.example.notepadapp.ui.components.cards

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notepadapp.database.models.Reminder
import com.example.notepadapp.database.models.RepeatMode
import com.example.notepadapp.helpers.DateHelper
import com.example.notepadapp.ui.modifiers.bounceClick
import com.example.notepadapp.ui.theme.CustomAppTheme
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
    val headerStyle = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp)
    val bodyStyle = MaterialTheme.typography.body1

    Card(
        modifier = modifier
            .bounceClick()
            //.heightIn(max = 250.dp, min = 50.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
        elevation = 0.dp,
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, if (selected) CustomAppTheme.colors.active else CustomAppTheme.colors.stroke),
        backgroundColor = CustomAppTheme.colors.secondaryBackground,
        contentColor = CustomAppTheme.colors.text,
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (markedText.isNullOrEmpty())
                NoteContent(header, text, headerStyle, bodyStyle)
            else
                NoteAnnotatedContent(header, text, markedText, headerStyle, bodyStyle)
            if (header.isNullOrEmpty() && text.isNullOrEmpty()) {
                Text(
                    text = "Empty note",
                    maxLines = 10,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1,
                    color = CustomAppTheme.colors.textSecondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            ReminderInformation(reminder)
        }
    }
}

private fun getNextReminderDate(reminder: Reminder?): LocalDateTime {
    if (reminder == null) return LocalDateTime.now()
    return DateHelper.nextDateWithRepeating(reminder.date, reminder.time, reminder.repeat)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ReminderInformation(reminder: Reminder?) {
    val nextReminderDate = remember { mutableStateOf(getNextReminderDate(reminder)) }
    val isDateFuture = remember { mutableStateOf(DateHelper.isFutureDateTime(nextReminderDate.value)) }
    if (reminder != null) {
        Spacer(modifier = Modifier.height(4.dp))
        val shape = RoundedCornerShape(5.dp)
        Row(
            Modifier
                .basicMarquee()
                .clip(shape)
                .background(CustomAppTheme.colors.secondaryBackground),
            //.padding(horizontal = 5.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                tint = CustomAppTheme.colors.textSecondary,
                imageVector = if (reminder.repeat == RepeatMode.NONE) Icons.Default.Event else Icons.Default.Repeat,
                contentDescription = "Event",
                modifier = Modifier.height(15.dp)
            )
            Spacer(Modifier.width(4.dp))
            androidx.compose.material3.Text(
                DateHelper.getLocalizedDate(nextReminderDate.value.toLocalDate()),
                style = MaterialTheme.typography.body1.copy(
                    fontSize = 13.sp,
                    textDecoration = if (isDateFuture.value) TextDecoration.None else TextDecoration.LineThrough
                ),
                color = CustomAppTheme.colors.textSecondary,
            )
            Spacer(Modifier.width(4.dp))
            if (isDateFuture.value)
                androidx.compose.material3.Text(
                    nextReminderDate.value.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = MaterialTheme.typography.body1.copy(fontSize = 13.sp),
                    color = CustomAppTheme.colors.textSecondary,
                )
        }
    }
}

@Composable
private fun NoteContent(header: String?, text: String?, headerStyle: TextStyle, bodyStyle: TextStyle) {
    var headerLineCount by remember { mutableStateOf(1) }
    if (!header.isNullOrEmpty()) {
        Text(
            text = header,
            style = headerStyle,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult: TextLayoutResult ->
                headerLineCount = textLayoutResult.lineCount
            },
            maxLines = 3
        )
    }
    if (!text.isNullOrEmpty() && !header.isNullOrEmpty())
        Spacer(modifier = Modifier.height(8.dp))
    if (!text.isNullOrEmpty()) {
        Text(
            text = text,
            maxLines = 8 - headerLineCount,
            overflow = TextOverflow.Ellipsis,
            style = bodyStyle,
            color = CustomAppTheme.colors.textSecondary,
        )
    }
}

@Composable
private fun NoteAnnotatedContent(
    header: String?,
    text: String?,
    markedText: String,
    headerStyle: TextStyle,
    bodyStyle: TextStyle
) {
    var headerLineCount by remember { mutableStateOf(1) }
    if (!header.isNullOrEmpty()) {
        Text(
            text = buildAnnotatedString(header, markedText, CustomAppTheme.colors.active, Color.Transparent),
            fontSize = headerStyle.fontSize,
            fontStyle = headerStyle.fontStyle,
            fontFamily = headerStyle.fontFamily,
            fontWeight = headerStyle.fontWeight,
            overflow = TextOverflow.Ellipsis,
            color = headerStyle.color,
            onTextLayout = { textLayoutResult: TextLayoutResult ->
                headerLineCount = textLayoutResult.lineCount
            },
            maxLines = 3,
        )
    }
    if (!text.isNullOrEmpty() && !header.isNullOrEmpty())
        Spacer(modifier = Modifier.height(8.dp))
    if (!text.isNullOrEmpty()) {
        Text(
            text = buildAnnotatedString(text, markedText, CustomAppTheme.colors.active, Color.Transparent),
            fontSize = bodyStyle.fontSize,
            fontStyle = bodyStyle.fontStyle,
            fontFamily = bodyStyle.fontFamily,
            fontWeight = bodyStyle.fontWeight,
            overflow = TextOverflow.Ellipsis,
            color = CustomAppTheme.colors.textSecondary,
            maxLines = 8 - headerLineCount,
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
            lastIndex += substring.length // Обновление значения lastIndex
        }
    }
}