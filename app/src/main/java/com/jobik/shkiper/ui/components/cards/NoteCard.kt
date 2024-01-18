package com.jobik.shkiper.ui.components.cards

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import com.jobik.shkiper.helpers.TextHelper
import com.jobik.shkiper.ui.helpers.MultipleEventsCutter
import com.jobik.shkiper.ui.helpers.get
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.CustomTheme
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
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
    val headerStyle = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
    val bodyStyle = MaterialTheme.typography.body1
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }
    val borderColor: Color by animateColorAsState(
        targetValue = if (selected) CustomTheme.colors.active else Color.Transparent, label = "borderColor",
    )

    Card(
        modifier = modifier
            .bounceClick()
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .combinedClickable(
                onClick = { multipleEventsCutter.processEvent { onClick() } },
                onLongClick = onLongClick,
            ),
        elevation = 0.dp,
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, borderColor),
        backgroundColor = CustomTheme.colors.secondaryBackground,
        contentColor = CustomTheme.colors.text,
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
                    text = stringResource(R.string.EmptyNote),
                    maxLines = 10,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1,
                    color = CustomTheme.colors.textSecondary,
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
                tint = CustomTheme.colors.textSecondary,
                imageVector = if (reminder.repeat == RepeatMode.NONE) Icons.Default.Event else Icons.Default.Repeat,
                contentDescription = stringResource(R.string.Event),
                modifier = Modifier.height(15.dp)
            )
            Spacer(Modifier.width(4.dp))
            androidx.compose.material3.Text(
                DateHelper.getLocalizedDate(nextReminderDate.toLocalDate()),
                style = MaterialTheme.typography.body1.copy(
                    fontSize = fontSize,
                    textDecoration = if (isDateFuture) TextDecoration.None else TextDecoration.LineThrough
                ),
                color = CustomTheme.colors.textSecondary,
            )
            Spacer(Modifier.width(4.dp))
            if (isDateFuture)
                androidx.compose.material3.Text(
                    nextReminderDate.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = MaterialTheme.typography.body1.copy(fontSize = fontSize),
                    color = CustomTheme.colors.textSecondary,
                )
        }
    }
}

@OptIn(ExperimentalRichTextApi::class)
@Composable
private fun NoteContent(header: String?, text: String?, headerStyle: TextStyle, bodyStyle: TextStyle) {
    var headerLineCount by remember { mutableStateOf(1) }
    val maxBodyLines = 8
    val richTextState = rememberRichTextState()

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

    LaunchedEffect(text) {
        if (text !== null && richTextState.annotatedString.text !== text)
            richTextState.setHtml(text)
        else
            richTextState.setText("")
    }

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
        Spacer(modifier = Modifier.height(4.dp))
    if (richTextState.annotatedString.text.isNotEmpty()) {
        Text(
            text = TextHelper.removeMarkdownStyles(richTextState.toMarkdown()),
            maxLines = maxBodyLines - headerLineCount,
            overflow = TextOverflow.Ellipsis,
            style = bodyStyle,
            color = CustomTheme.colors.textSecondary,
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
    text: String?,
    markedText: String,
    headerStyle: TextStyle,
    bodyStyle: TextStyle
) {
    var headerLineCount by remember { mutableStateOf(1) }
    val maxBodyLines = 8
    val richTextState = rememberRichTextState()

    LaunchedEffect(text) {
        if (text !== null && richTextState.annotatedString.text !== text)
            richTextState.setHtml(text)
        else
            richTextState.setText("")
    }

    if (!header.isNullOrEmpty()) {
        Text(
            text = buildAnnotatedString(header, markedText, CustomTheme.colors.active, Color.Transparent),
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
    if (richTextState.annotatedString.text.isNotEmpty()) {
        Text(
            text = buildAnnotatedString(
                TextHelper.removeMarkdownStyles(richTextState.toMarkdown()),
                markedText,
                CustomTheme.colors.active,
                Color.Transparent
            ),
            fontSize = bodyStyle.fontSize,
            fontStyle = bodyStyle.fontStyle,
            fontFamily = bodyStyle.fontFamily,
            fontWeight = bodyStyle.fontWeight,
            overflow = TextOverflow.Ellipsis,
            color = CustomTheme.colors.textSecondary,
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
            lastIndex += substring.length // Обновление значения lastIndex
        }
    }
}