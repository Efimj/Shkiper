package com.jobik.shkiper.widgets.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import com.jobik.shkiper.SharedPreferencesKeys
import com.jobik.shkiper.database.data.note.NoteMongoRepository
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.widgets.WidgetKeys
import com.jobik.shkiper.widgets.WidgetKeys.Prefs.noteBody
import com.jobik.shkiper.widgets.WidgetKeys.Prefs.noteHeader
import com.jobik.shkiper.widgets.WidgetKeys.Prefs.noteId
import com.jobik.shkiper.widgets.WidgetKeys.Prefs.noteLastUpdate
import com.jobik.shkiper.widgets.widgets.NoteWidget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

@AndroidEntryPoint
class PinWidgetReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: NoteMongoRepository

    override fun onReceive(context: Context, intent: Intent) {
        val noteId = intent.getStringExtra(SharedPreferencesKeys.NoteIdExtra)
        if(noteId.isNullOrEmpty()) return
        CoroutineScope(EmptyCoroutineContext).launch {
            delay(3000)
            val note = repository.getNote(ObjectId(noteId)) ?: return@launch
            val glanceManager = GlanceAppWidgetManager(context)
            val lastAddedGlanceId = glanceManager.getGlanceIds(NoteWidget::class.java).last()
            mapNoteToWidget(context, lastAddedGlanceId, note)
        }
    }

    private suspend fun mapNoteToWidget(context: Context, lastAddedGlanceId: GlanceId, note: Note) {
        updateAppWidgetState(context, lastAddedGlanceId) { prefs ->
            prefs[noteId] = note._id.toHexString()
            prefs[noteHeader] = note.header
            prefs[noteBody] = note.body
            prefs[noteLastUpdate] = note.updateDateString
        }
        NoteWidget().update(context, lastAddedGlanceId)
    }
}