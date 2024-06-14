package com.jobik.shkiper.widgets.components

import android.content.Context
import android.os.Build
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
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
import com.jobik.shkiper.SharedPreferencesKeys
import com.jobik.shkiper.activity.StartupActivity
import com.jobik.shkiper.helpers.DateHelper
import com.jobik.shkiper.ui.theme.CustomThemeColors
import com.jobik.shkiper.ui.theme.CustomThemeStyle
import com.jobik.shkiper.ui.theme.getDynamicColors
import com.jobik.shkiper.util.ThemeUtil
import com.jobik.shkiper.widgets.WidgetKeys.Prefs.noteBody
import com.jobik.shkiper.widgets.WidgetKeys.Prefs.noteHeader
import com.jobik.shkiper.widgets.WidgetKeys.Prefs.noteId
import com.jobik.shkiper.widgets.WidgetKeys.Prefs.noteLastUpdate
import java.time.LocalDateTime

@Composable
private fun getThemeColors(
    style: CustomThemeStyle,
    darkTheme: Boolean,
    context: Context
): CustomThemeColors {
    val colors = when {
        style == CustomThemeStyle.MaterialDynamicColors && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            getDynamicColors(darkTheme = darkTheme, context = context)

        darkTheme -> style.dark
        else -> style.light
    }
    return colors
}

@Composable
fun NoteWidgetContent(prefs: Preferences) {
    ThemeUtil.restoreSavedTheme(LocalContext.current)

    val userStyles = ThemeUtil.themeStyle.value ?: CustomThemeStyle.PastelPurple
    val darkColors = getThemeColors(style = userStyles, darkTheme = true, LocalContext.current)
    val lightColors = getThemeColors(style = userStyles, darkTheme = false, LocalContext.current)

    val noteId = prefs[noteId].orEmpty()
    val noteHeader = prefs[noteHeader].orEmpty()
    val noteBody = prefs[noteBody].orEmpty()
    val updatedAt = prefs[noteLastUpdate].orEmpty()
    val updatedDateString = getLocalizedDateTime(updatedAt)

    val contentPadding = 12.dp

    Box(
        modifier = GlanceModifier
            .background(ColorProvider(day = lightColors.container, night = darkColors.container))
            .cornerRadius(12.dp)
            .appWidgetBackground()
    ) {
        LazyColumn(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                Spacer(modifier = GlanceModifier.fillMaxWidth().height(contentPadding))
            }
            if (noteHeader.isNotEmpty()) item {
                Text(
                    text = noteHeader,
                    modifier = GlanceModifier.openNote(noteId).fillMaxWidth().padding(horizontal = contentPadding),
                    style = TextStyle(
                        color = ColorProvider(day = lightColors.onSecondaryContainer, night = darkColors.onSecondaryContainer),
                        fontFamily = FontFamily("Roboto"),
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                )
            }
            if (noteBody.isNotEmpty() && noteHeader.isNotEmpty()) item {
                Spacer(modifier = GlanceModifier.openNote(noteId).fillMaxWidth().height(3.dp))
            }
            if (noteBody.isNotEmpty()) item {
                Text(
                    text = noteBody,
                    modifier = GlanceModifier.openNote(noteId).fillMaxWidth().padding(horizontal = contentPadding),
                    style = TextStyle(
                        color = ColorProvider(day = lightColors.text, night = darkColors.text),
                        fontFamily = FontFamily("Roboto"),
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                )
            }
            if (noteBody.isNotEmpty() && updatedDateString != null) item {
                Text(
                    text = updatedDateString.toString(),
                    modifier = GlanceModifier.openNote(noteId).fillMaxWidth().padding(end = contentPadding)
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                    style = TextStyle(
                        color = ColorProvider(day = lightColors.textSecondary, night = darkColors.textSecondary),
                        fontFamily = FontFamily("Roboto"),
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.End
                    )
                )
            }
            item {
                Spacer(modifier = GlanceModifier.height(contentPadding))
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun GlanceModifier.openNote(noteId: String) =
    if (noteId.isNotEmpty())
        this.clickable(
            actionStartActivity<StartupActivity>(
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