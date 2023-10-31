package com.jobik.shkiper.widgets.config

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.lifecycle.lifecycleScope
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.ui.theme.CustomTheme
import com.jobik.shkiper.ui.theme.CustomThemeStyle
import com.jobik.shkiper.ui.theme.ShkiperTheme
import com.jobik.shkiper.util.ThemeUtil
import com.jobik.shkiper.widgets.WidgetKeys
import com.jobik.shkiper.widgets.screens.NoteSelectionScreen.NoteSelectionScreen
import com.jobik.shkiper.widgets.widgets.NoteWidget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConfigWidgetActivity : AppCompatActivity() {

    private var widgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    private val result = Intent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivity()
        ThemeUtil.restoreSavedTheme(this)
        setContent {
            ShkiperTheme(
                darkTheme = ThemeUtil.isDarkMode.value ?: isSystemInDarkTheme(),
                style = ThemeUtil.themeStyle.value ?: CustomThemeStyle.DarkPurple
            ) {
                Box(
                    Modifier.fillMaxSize().background(CustomTheme.colors.mainBackground)
                ) {
                    NoteSelectionScreen {
                        handleSelectNote(it)
                    }
                }
            }
        }
    }

    private fun handleSelectNote(note: Note) {
        setResult(RESULT_OK, result)
        finish()
        saveWidgetState(note)
    }

    private fun saveWidgetState(note: Note) = lifecycleScope.launch(Dispatchers.IO) {
        val glanceId = GlanceAppWidgetManager(applicationContext).getGlanceIdBy(widgetId)
        updateAppWidgetState(application.applicationContext, glanceId) { prefs ->
            prefs[WidgetKeys.Prefs.noteId] = note._id.toHexString()
            prefs[WidgetKeys.Prefs.noteHeader] = note.header
            prefs[WidgetKeys.Prefs.noteBody] = note.body
            prefs[WidgetKeys.Prefs.noteLastUpdate] = note.updateDateString
        }
        NoteWidget().update(application.applicationContext, glanceId)
    }

    private fun setupActivity() {
        setResult(RESULT_CANCELED, result)
        getWidgetId()
        initResult()
    }

    private fun getWidgetId() {
        widgetId = intent.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: return
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) finish()
    }

    private fun initResult() = result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
}