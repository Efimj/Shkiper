package com.example.notepadapp.ui.components.cards

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notepadapp.helpers.DateHelper
import com.example.notepadapp.ui.theme.CustomAppTheme
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    header: String? = null,
    text: String? = null,
    reminderDate: LocalDate? = null,
    markedText: String? = null,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val headerStyle = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
    val bodyStyle = MaterialTheme.typography.body1

    Card(
        modifier = modifier
            .heightIn(max = 250.dp, min = 50.dp)
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
                noteContent(header, text, headerStyle, bodyStyle)
            else
                noteAnnotatedContent(header, text, markedText, headerStyle, bodyStyle)
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
            if (reminderDate != null) {
                Spacer(modifier = Modifier.height(4.dp))
                val shape = RoundedCornerShape(5.dp)
                Row(
                    Modifier
                        .clip(shape)
                        .background(CustomAppTheme.colors.secondaryBackground)
                        .padding(horizontal = 5.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        tint = CustomAppTheme.colors.textSecondary,
                        imageVector = Icons.Default.Event,
                        contentDescription = "Event",
                        modifier = Modifier.height(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    androidx.compose.material3.Text(
                        DateHelper.getLocalizedDate(reminderDate),
                        style = MaterialTheme.typography.body1.copy(fontSize = 15.sp),
                        color = CustomAppTheme.colors.textSecondary,
                    )
                }
            }
        }
    }
}

@Composable
private fun noteContent(header: String?, text: String?, headerStyle: TextStyle, bodyStyle: TextStyle) {
    if (!header.isNullOrEmpty()) {
        Text(
            text = header,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
            style = headerStyle,
            fontSize = 18.sp
        )
    }
    if (!text.isNullOrEmpty() && !header.isNullOrEmpty())
        Spacer(modifier = Modifier.height(8.dp))
    if (!text.isNullOrEmpty()) {
        Text(
            text = text,
            maxLines = 10,
            overflow = TextOverflow.Ellipsis,
            style = bodyStyle,
            color = CustomAppTheme.colors.textSecondary,
        )
    }
}

@Composable
private fun noteAnnotatedContent(
    header: String?,
    text: String?,
    markedText: String,
    headerStyle: TextStyle,
    bodyStyle: TextStyle
) {
    if (!header.isNullOrEmpty()) {
        Text(
            text = buildAnnotatedString(header, markedText, CustomAppTheme.colors.active, Color.Transparent),
            fontSize = headerStyle.fontSize,
            fontStyle = headerStyle.fontStyle,
            fontFamily = headerStyle.fontFamily,
            fontWeight = headerStyle.fontWeight,
            color = headerStyle.color
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
            color = CustomAppTheme.colors.textSecondary
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