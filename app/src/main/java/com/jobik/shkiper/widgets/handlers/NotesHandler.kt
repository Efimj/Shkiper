package com.jobik.shkiper.widgets.handlers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.Preferences
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateIf
import com.jobik.shkiper.SharedPreferencesKeys
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.widgets.WidgetKeys
import com.jobik.shkiper.widgets.WidgetKeys.Prefs.noteId
import com.jobik.shkiper.widgets.services.NoteWidgetReceiver
import com.jobik.shkiper.widgets.services.PinWidgetReceiver
import com.jobik.shkiper.widgets.widgets.NoteWidget
import org.mongodb.kbson.ObjectId

class NotesHandler {

}

suspend fun handleNoteWidgetPin(context: Context, noteId: String) {
    val intent = Intent(context, PinWidgetReceiver::class.java)
    intent.putExtra(SharedPreferencesKeys.NoteIdExtra, noteId)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        ObjectId(noteId).timestamp,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )
    GlanceAppWidgetManager(context).requestPinGlanceAppWidget(
        NoteWidgetReceiver::class.java,
        successCallback = pendingIntent
    )
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