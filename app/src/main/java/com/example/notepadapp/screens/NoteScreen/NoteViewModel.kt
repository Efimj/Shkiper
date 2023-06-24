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
import java.time.LocalDateTime
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

    /*******************
     * Screen States
     *******************/

    private var _isTopAppBarHover = mutableStateOf(false)
    val isTopAppBarHover: State<Boolean> = _isTopAppBarHover

    private var _isBottomAppBarHover = mutableStateOf(false)
    val isBottomAppBarHover: State<Boolean> = _isBottomAppBarHover

    fun setTopAppBarHover(isHover: Boolean) {
        _isTopAppBarHover.value = isHover
    }

    fun setBottomAppBarHover(isHover: Boolean) {
        _isBottomAppBarHover.value = isHover
    }

    /*******************
     * Note
     *******************/

    private var _noteId = mutableStateOf(ObjectId(savedStateHandle[ARGUMENT_NOTE_ID] ?: ""))
    val noteId: State<ObjectId> = _noteId

    val note = noteRepository.getNote(noteId.value)

    private var _noteHeader = mutableStateOf(note?.header ?: "")
    val noteHeader: State<String> = _noteHeader

    private var _noteBody = mutableStateOf(note?.body ?: "")
    val noteBody: State<String> = _noteBody

    private var _noteIsPinned = mutableStateOf(note?.isPinned ?: false)
    val noteIsPinned: State<Boolean> = _noteIsPinned

    private var _noteUpdatedDate = mutableStateOf(note?.updateDate ?: LocalDateTime.now())
    val noteUpdatedDate: State<LocalDateTime> = _noteUpdatedDate

    fun updateNoteHeader(text: String) {
        _noteHeader.value = text
        _noteUpdatedDate.value = LocalDateTime.now()
        updateNote {
            it.header = this@NoteViewModel._noteHeader.value
            it.updateDate = this@NoteViewModel._noteUpdatedDate.value
        }
    }

    fun updateNoteBody(text: String) {
        _noteBody.value = text
        _noteUpdatedDate.value = LocalDateTime.now()
        updateNote {
            it.body = this@NoteViewModel._noteBody.value
            it.updateDate = this@NoteViewModel._noteUpdatedDate.value
        }
    }

    fun switchNotePinnedMode() {
        _noteIsPinned.value = !noteIsPinned.value
        updateNote {
            it.isPinned = this@NoteViewModel.noteIsPinned.value
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
            if (noteHeader.value.isEmpty() && noteBody.value.isEmpty())
                noteRepository.deleteNote(_noteId.value, application.applicationContext)
        }
    }

    fun saveChanges() {
        updateNote {
            it.header = this@NoteViewModel._noteHeader.value
            it.body = this@NoteViewModel._noteBody.value
            it.updateDate = this@NoteViewModel._noteUpdatedDate.value
            it.isPinned = this@NoteViewModel.noteIsPinned.value
        }
    }

    /*******************
     * Reminder region
     *******************/

    private var _reminder = mutableStateOf<Reminder?>(null)
    val reminder: State<Reminder?> = _reminder

    private fun getReminder() {
        viewModelScope.launch {
            val reminderValue = reminderRepository.getReminderForNote(_noteId.value)
            _reminder.value = reminderValue
        }
    }

    fun createReminder(date: LocalDate, time: LocalTime, repeatMode: RepeatMode) {
        if (DateHelper.isFutureDateTime(date, time)) {
            viewModelScope.launch {
                val note = noteRepository.getNote(_noteId.value) ?: return@launch
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