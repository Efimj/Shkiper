package com.jobik.shkiper.ui.components.cards

import android.os.Parcelable
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.database.models.Reminder
import com.jobik.shkiper.database.models.RepeatMode
import com.jobik.shkiper.helpers.DateHelper
import com.jobik.shkiper.helpers.TextHelper.Companion.removeMarkdownStyles
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.modifiers.sharedNoteTransitionModifier
import com.jobik.shkiper.ui.modifiers.skipToLookaheadSize
import com.jobik.shkiper.ui.theme.AppTheme
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class NoteSharedElementKey(
    val noteId: String,
    val origin: String = "main",
    val type: NoteSharedElementType
)

enum class NoteSharedElementType {
    Bounds
}

@Parcelize
private data class NoteCardState(
    val header: String? = null,
    val htmlBody: String? = null,
    val body: String? = null,
    val reminderDate: LocalDateTime? = null,
    val repeatMode: RepeatMode? = null,
) : Parcelable

private fun updateNoteCardState(
    currentState: NoteCardState?,
    header: String?,
    htmlText: String?,
    reminder: Reminder?
): NoteCardState {
    var newHeader: String? = null
    newHeader = if (currentState == null || currentState.header != header) {
        header
    } else {
        currentState.header
    }

    var newBodyHtml: String? = null
    var newBody: String? = null
    if (currentState == null || currentState.htmlBody != htmlText) {
        if (htmlText != null) {
            val richText = RichTextState()
            richText.setHtml(htmlText)
            newBody = removeMarkdownStyles(richText.toMarkdown())
            newBodyHtml = htmlText
        }
    } else {
        newBody = currentState.body
        newBodyHtml = currentState.htmlBody
    }

    val newReminderDate = if (reminder != null) getNextReminderDate(reminder) else null
    val newRepeatMode = reminder?.repeat

    return NoteCardState(
        header = newHeader,
        body = newBody,
        htmlBody = newBodyHtml,
        reminderDate = newReminderDate,
        repeatMode = newRepeatMode
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun NoteCard(
    note: Note,
    reminder: Reminder? = null,
    markedText: String? = null,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val headerStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
    val bodyStyle = MaterialTheme.typography.bodyMedium
    val borderColor by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.primary else Color.Transparent,
        label = "borderColor",
    )

    val cardState = rememberSaveable { mutableStateOf<NoteCardState?>(null) }

    LaunchedEffect(note.header, note.body, reminder) {
        val newCardState = updateNoteCardState(
            currentState = cardState.value,
            header = note.header,
            htmlText = note.body,
            reminder = reminder
        )
        cardState.value = newCardState
    }

    Card(
        modifier = modifier
            .sharedNoteTransitionModifier(noteId = note._id.toHexString())
            .bounceClick()
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .combinedClickable(
                onClick = onClick,
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
            if (cardState.value != null) {
                if (markedText.isNullOrBlank()) {
                    NoteContent(
                        cardState = cardState.value!!,
                        headerStyle = headerStyle,
                        bodyStyle = bodyStyle
                    )
                } else {
                    NoteAnnotatedContent(
                        cardState = cardState.value!!,
                        markedText = markedText,
                        headerStyle = headerStyle,
                        bodyStyle = bodyStyle
                    )
                }
                if (cardState.value!!.header.isNullOrBlank() && cardState.value!!.body.isNullOrBlank()) {
                    EmptyNoteContent(bodyStyle = bodyStyle)
                }
                ReminderInformation(cardState = cardState.value!!)
            }
        }
    }
}

@Composable
private fun ColumnScope.EmptyNoteContent(
    bodyStyle: TextStyle
) {
    Text(
        modifier = Modifier
            .skipToLookaheadSize()
            .align(Alignment.CenterHorizontally),
        text = stringResource(R.string.EmptyNote),
        maxLines = 10,
        overflow = TextOverflow.Ellipsis,
        style = bodyStyle,
        color = AppTheme.colors.textSecondary,
    )
}

private fun getNextReminderDate(reminder: Reminder?): LocalDateTime {
    if (reminder == null) return LocalDateTime.now()
    return DateHelper.nextDateWithRepeating(
        notificationDate = LocalDateTime.of(reminder.date, reminder.time),
        repeatMode = reminder.repeat
    )
}

@Composable
private fun ReminderInformation(
    cardState: NoteCardState,
) {
    if (cardState.reminderDate != null) {
        val isDateFuture = DateHelper.isFutureDateTime(cardState.reminderDate)

        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .basicMarquee(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                tint = AppTheme.colors.textSecondary,
                imageVector = if (cardState.repeatMode == RepeatMode.NONE) Icons.Default.Event else Icons.Default.Repeat,
                contentDescription = stringResource(R.string.Event),
                modifier = Modifier.height(15.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                modifier = Modifier.skipToLookaheadSize(),
                text = DateHelper.getLocalizedDate(cardState.reminderDate.toLocalDate()),
                style = MaterialTheme.typography.bodySmall.copy(
                    textDecoration = if (isDateFuture) TextDecoration.None else TextDecoration.LineThrough
                ),
                color = AppTheme.colors.textSecondary,
            )
            Spacer(Modifier.width(4.dp))
            if (isDateFuture)
                Text(
                    modifier = Modifier.skipToLookaheadSize(),
                    text = cardState.reminderDate.toLocalTime()
                        .format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.colors.textSecondary,
                )
        }
    }
}

@Composable
private fun NoteContent(
    cardState: NoteCardState,
    headerStyle: TextStyle,
    bodyStyle: TextStyle
) {
    var headerLineCount by rememberSaveable { mutableIntStateOf(1) }
    val maxBodyLines = 8

    if (!cardState.header.isNullOrBlank()) {
        Text(
            modifier = Modifier.skipToLookaheadSize(),
            text = cardState.header,
            style = headerStyle,
            overflow = TextOverflow.Ellipsis,
            color = AppTheme.colors.onSecondaryContainer,
            onTextLayout = { textLayoutResult: TextLayoutResult ->
                headerLineCount = textLayoutResult.lineCount
            },
            maxLines = 3
        )
    }
    if (!cardState.header.isNullOrBlank() && !cardState.body.isNullOrBlank())
        Spacer(modifier = Modifier.height(4.dp))
    if (!cardState.body.isNullOrBlank()) {
        Text(
            modifier = Modifier.skipToLookaheadSize(),
            text = cardState.body,
            maxLines = maxBodyLines - headerLineCount,
            overflow = TextOverflow.Ellipsis,
            style = bodyStyle,
            color = AppTheme.colors.textSecondary,
        )
    }
}

@Composable
private fun NoteAnnotatedContent(
    cardState: NoteCardState,
    markedText: String,
    headerStyle: TextStyle,
    bodyStyle: TextStyle
) {
    var headerLineCount by rememberSaveable { mutableIntStateOf(1) }
    val maxBodyLines = 8

    if (!cardState.header.isNullOrBlank()) {
        Text(
            modifier = Modifier.skipToLookaheadSize(),
            text = buildAnnotatedString(
                text = cardState.header,
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
    if (!cardState.header.isNullOrBlank() && !cardState.body.isNullOrBlank())
        Spacer(modifier = Modifier.height(4.dp))
    if (!cardState.body.isNullOrBlank()) {
        Text(
            modifier = Modifier.skipToLookaheadSize(),
            text = buildAnnotatedString(
                text = cardState.body,
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

fun buildAnnotatedString(
    text: String,
    substring: String,
    color: Color,
    background: Color
): AnnotatedString {
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