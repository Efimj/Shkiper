package com.jobik.shkiper.widgets.handlers

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateIf
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.widgets.WidgetKeys
import com.jobik.shkiper.widgets.WidgetKeys.Prefs.noteId
import com.jobik.shkiper.widgets.widgets.NoteWidget

class NotesHandler {

}

suspend fun GlanceAppWidgetManager.mapNoteToWidget(context: Context, note: Note) =
    getGlanceIds(NoteWidget::class.java)
        .forEach { glanceId ->
            updateAppWidgetState(context, glanceId) { prefs ->
                if (prefs[noteId] == note._id.toHexString()) {
                    prefs[WidgetKeys.Prefs.noteHeader] = note.header
                    prefs[WidgetKeys.Prefs.noteBody] = note.body
                    prefs[WidgetKeys.Prefs.noteLastUpdate] = note.updateDateString
                    NoteWidget().update(context, glanceId)
                }
            }
            NoteWidget().updateIf<Preferences>(context) {
                it[noteId] == note._id.toHexString()
            }
        }

suspend fun GlanceAppWidgetManager.deleteNoteFromWidget(context: Context, id: String) =
    getGlanceIds(NoteWidget::class.java)
        .forEach { glanceId ->
            updateAppWidgetState(context, glanceId) { prefs ->
                if (prefs[noteId] == id) {
                    prefs[noteId] = ""
                }
            }
            NoteWidget().updateIf<Preferences>(context) {
                it[noteId] == id
            }
        }