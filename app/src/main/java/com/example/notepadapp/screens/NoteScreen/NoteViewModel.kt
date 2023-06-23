package com.example.notepadapp.screens.NoteScreen

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepadapp.database.data.note.NoteMongoRepository
import com.example.notepadapp.database.data.reminder.ReminderMongoRepository
import com.example.notepadapp.database.models.Note
import com.example.notepadapp.database.models.Reminder
import com.example.notepadapp.database.models.RepeatMode
import com.example.notepadapp.helpers.DateHelper
import com.example.notepadapp.navigation.ARGUMENT_NOTE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val application: Application,
    private val noteRepository: NoteMongoRepository,
    private val reminderRepository: ReminderMongoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var isTopAppBarHover by mutableStateOf(false)
    var isBottomAppBarHover by mutableStateOf(false)

    var noteId by mutableStateOf(ObjectId(savedStateHandle[ARGUMENT_NOTE_ID] ?: ""))
    val note = noteRepository.getNote(noteId)
    var noteHeader by mutableStateOf(note?.header ?: "")
    var noteBody by mutableStateOf(note?.body ?: "")
    var noteIsPinned by mutableStateOf(note?.isPinned ?: false)
    var noteUpdatedDate by mutableStateOf(note?.updateDate ?: Date())

    fun updateNoteHeader(text: String) {
        noteHeader = text
        noteUpdatedDate = Date()
        updateNote {
            it.header = this@NoteViewModel.noteHeader
            it.updateDate = this@NoteViewModel.noteUpdatedDate
        }
    }

    fun updateNoteBody(text: String) {
        noteBody = text
        noteUpdatedDate = Date()
        updateNote {
            it.body = this@NoteViewModel.noteBody
            it.updateDate = this@NoteViewModel.noteUpdatedDate
        }
    }

    fun switchNotePinnedMode() {
        noteIsPinned = !noteIsPinned
        updateNote {
            it.isPinned = this@NoteViewModel.noteIsPinned
        }
    }

    private fun updateNote(updateParams: (Note) -> Unit) {
        if (this@NoteViewModel.note == null) return
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.updateNote(this@NoteViewModel.note._id, application.applicationContext, updateParams)
        }
    }

    fun deleteNoteIfEmpty() {
        viewModelScope.launch {
            if (noteHeader.isEmpty() && noteBody.isEmpty())
                noteRepository.deleteNote(noteId, application.applicationContext)
        }
    }

    fun saveChanges() {
        updateNote {
            it.header = this@NoteViewModel.noteHeader
            it.body = this@NoteViewModel.noteBody
            it.updateDate = this@NoteViewModel.noteUpdatedDate
            it.isPinned = this@NoteViewModel.noteIsPinned
        }
    }

    /*******************
     * Reminder region
     *******************/

    private var _reminder = mutableStateOf<Reminder?>(null)
    val reminder: State<Reminder?> = _reminder

    private fun getReminder() {
        viewModelScope.launch {
            val reminderValue = reminderRepository.getReminderForNote(noteId)
            _reminder.value = reminderValue
        }
    }

    fun createReminder(date: LocalDate, time: LocalTime, repeatMode: RepeatMode) {
        if (DateHelper.isFutureDateTime(date, time)) {
            viewModelScope.launch {
                val note = noteRepository.getNote(noteId) ?: return@launch
                val noteList = listOf(note)
                reminderRepository.updateOrCreateReminderForNotes(
                    noteList, application.applicationContext
                ) { updatedReminder ->
                    updatedReminder.date = date
                    updatedReminder.time = time
                    updatedReminder.repeat = repeatMode
                }
                getReminder()
            }
            switchReminderDialogShow()
        }
    }

    fun deleteReminder() {
        viewModelScope.launch {
            val reminderId = reminder.value?._id ?: return@launch
            reminderRepository.deleteReminder(reminderId, application.applicationContext)
            _reminder.value = null
        }
        switchReminderDialogShow()
    }

    private val _isCreateReminderDialogShow = mutableStateOf(false)
    val isCreateReminderDialogShow: State<Boolean> = _isCreateReminderDialogShow

    fun switchReminderDialogShow() {
        _isCreateReminderDialogShow.value = !_isCreateReminderDialogShow.value
    }

    init {
        viewModelScope.launch {
            getReminder()
        }
    }
}