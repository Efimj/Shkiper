package com.jobik.shkiper.widgets.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.*
import androidx.glance.text.*
import com.jobik.shkiper.R
import com.jobik.shkiper.SharedPreferencesKeys
import com.jobik.shkiper.activity.MainActivity
import com.jobik.shkiper.helpers.DateHelper
import com.jobik.shkiper.ui.theme.DarkDeepPurple
import com.jobik.shkiper.ui.theme.LightDeepPurple
import com.jobik.shkiper.widgets.WidgetKeys.Prefs.noteBody
import com.jobik.shkiper.widgets.WidgetKeys.Prefs.noteHeader
import com.jobik.shkiper.widgets.WidgetKeys.Prefs.noteId
import com.jobik.shkiper.widgets.WidgetKeys.Prefs.noteLastUpdate
import java.time.LocalDateTime

@Composable
fun NoteWidgetContent(prefs: Preferences) {
    val darkColors = DarkDeepPurple
    val lightColors = LightDeepPurple

    val noteId = prefs[noteId].orEmpty()
    val noteHeader = prefs[noteHeader].orEmpty()
    val noteBody = prefs[noteBody].orEmpty()
    val updatedAt = prefs[noteLastUpdate].orEmpty()
    val updatedDateString = getLocalizedDateTime(updatedAt)

    Box(
        modifier = GlanceModifier
            .background(ImageProvider(R.drawable.widget_background))
            .cornerRadius(15.dp)
            .appWidgetBackground()
    ) {
        LazyColumn(modifier = GlanceModifier) {
            item {
                Spacer(modifier = GlanceModifier.height(16.dp))
            }
            if (noteHeader.isNotEmpty()) item {
                Text(
                    text = noteHeader,
                    modifier = GlanceModifier.openNote(noteId).fillMaxWidth().padding(horizontal = 16.dp),
                    style = TextStyle(
                        color = ColorProvider(day = lightColors.text, night = darkColors.text),
                        fontFamily = FontFamily("Roboto"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            if (noteBody.isNotEmpty() && noteHeader.isNotEmpty()) item {
                Spacer(modifier = GlanceModifier.openNote(noteId).fillMaxWidth().height(3.dp))
            }
            if (noteBody.isNotEmpty()) item {
                Text(
                    text = noteBody,
                    modifier = GlanceModifier.openNote(noteId).fillMaxWidth().padding(horizontal = 16.dp),
                    style = TextStyle(
                        color = ColorProvider(day = lightColors.text, night = darkColors.text),
                        fontFamily = FontFamily("Roboto"),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
            if (noteBody.isNotEmpty() && updatedDateString != null) item {
                Text(
                    text = updatedDateString.toString(),
                    modifier = GlanceModifier.openNote(noteId).fillMaxWidth().padding(end = 16.dp)
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                    style = TextStyle(
                        color = ColorProvider(day = lightColors.textSecondary, night = darkColors.textSecondary),
                        fontFamily = FontFamily("Roboto"),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Right
                    )
                )
            }
            item {
                Spacer(modifier = GlanceModifier.height(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun GlanceModifier.openNote(noteId: String) =
    if (noteId.isNotEmpty())
        this.clickable(
            actionStartActivity<MainActivity>(
                parameters = actionParametersOf(
                    ActionParameters.Key<String>(SharedPreferencesKeys.NoteIdExtra) to noteId
                )
            )
        ) else this


private fun getLocalizedDateTime(dateTime: String): String? {
    return try {
        DateHelper.getLocalizedDate(LocalDateTime.parse(dateTime))
    } catch (e: Exception) {
        null
    }
}