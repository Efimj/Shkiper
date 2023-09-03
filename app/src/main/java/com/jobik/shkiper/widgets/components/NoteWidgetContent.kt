package com.jobik.shkiper.widgets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.jobik.shkiper.R
import com.jobik.shkiper.widgets.WidgetKeys.Prefs.noteBody
import com.jobik.shkiper.widgets.WidgetKeys.Prefs.noteHeader
import com.jobik.shkiper.widgets.WidgetKeys.Prefs.noteId
import com.jobik.shkiper.widgets.WidgetKeys.Prefs.noteLastUpdate
import androidx.datastore.preferences.core.Preferences
import androidx.glance.ImageProvider

@Composable
fun NoteWidgetContent(prefs: Preferences) {

    val noteId = prefs[noteId].orEmpty()
    val noteTitle = prefs[noteHeader].orEmpty()
    val noteText = prefs[noteBody].orEmpty()
    val updatedAt = prefs[noteLastUpdate].orEmpty()

    LazyColumn(
        modifier = GlanceModifier
            .background(imageProvider = ImageProvider(R.drawable.widget_background))
            .appWidgetBackground()
            .padding(16.dp)

    ) {
        if (noteTitle.isNotEmpty()) item {
            WidgetText(noteTitle, noteId)
        }
        if (noteText.isNotEmpty()) item {
            WidgetText(noteText, noteId)
        }
        if (updatedAt.isNotEmpty()) item {
            WidgetText(updatedAt, noteId, 16.sp)
        }
    }
}

@Composable
fun WidgetText(text: String, noteId: String, fontSize: TextUnit = 20.sp) {
    Text(
        text = text,
        style = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = fontSize,
            textAlign = TextAlign.Start,
            color = ColorProvider(
                day = Color.White,
                night = Color.White
            )
        )
    )
}