package com.example.notepadapp.screens.NoteListScreen

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepadapp.R
import com.example.notepadapp.database.data.note.NoteMongoRepository
import com.example.notepadapp.database.data.reminder.ReminderMongoRepository
import com.example.notepadapp.database.models.Note
import com.example.notepadapp.database.models.Reminder
import com.example.notepadapp.database.models.RepeatMode
import com.example.notepadapp.helpers.DateHelper
import com.example.notepadapp.notification_service.NotificationData
import com.example.notepadapp.notification_service.NotificationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import java.time.*
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteMongoRepository,
    private val reminderRepository: ReminderMongoRepository,
    private val application: Application
) : ViewModel() {
    /*******************
     * Notes region
     *******************/
    val pinnedNotes = mutableStateOf(emptyList<Note>())
    val notes = mutableStateOf(emptyList<Note>())
    var lastCreatedNoteId by mutableStateOf("")
    var searchText by mutableStateOf("")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getNotes()
            getReminders()
        }
    }

    private fun getNotes() {
        viewModelScope.launch {
            noteRepository.getNotes(true).collect() {
                pinnedNotes.value = it
            }
        }
        viewModelScope.launch {
            noteRepository.getNotes().collect() {
                notes.value = it
            }
        }
    }

    private val _selectedNoteCardIndices = mutableStateOf(setOf<ObjectId>())
    val selectedNotes: State<Set<ObjectId>> = _selectedNoteCardIndices

    fun clearSelectedNote() {
        _selectedNoteCardIndices.value = setOf()
    }

    fun changeSearchText(newString: String) {
        searchText = newString
        viewModelScope.launch(Dispatchers.IO) {
            if (newString.isEmpty()) {
                getNotes()
            } else {
                getNotesByText(searchText)
            }
        }
    }

    private suspend fun getNotesByText(newString: String) {
        pinnedNotes.value = emptyList()
        noteRepository.filterNotesByContains(newString).collect {
            notes.value = it
        }
    }

    fun toggleSelectedNoteCard(index: ObjectId) {
        _selectedNoteCardIndices.value =
            if (_selectedNoteCardIndices.value.contains(index)) _selectedNoteCardIndices.value.minus(index)
            else _selectedNoteCardIndices.value.plus(index)
    }

    fun deleteSelectedNotes() {
        viewModelScope.launch {
            noteRepository.deleteNote(selectedNotes.value.toList(), application.applicationContext)
            clearSelectedNote()
        }
    }

    fun pinSelectedNotes() {
        viewModelScope.launch {
            val pinMode: Boolean
            var unpinnedNote = notes.value.find { note ->
                selectedNotes.value.any {
                    it == note._id
                } && !note.isPinned
            }
            if (unpinnedNote == null) {
                unpinnedNote = pinnedNotes.value.find { note ->
                    selectedNotes.value.any {
                        it == note._id
                    } && !note.isPinned
                }
            }
            pinMode = unpinnedNote != null
            noteRepository.updateNote(selectedNotes.value.toList(), application.applicationContext) { updatedNote ->
                updatedNote.isPinned = pinMode
            }
            clearSelectedNote()
        }
    }

    fun createNewNote() {
        viewModelScope.launch(Dispatchers.IO) {
            val newNote = Note()
            noteRepository.insertNote(newNote)
            lastCreatedNoteId = newNote._id.toHexString()
        }
    }

    /*******************
     * Reminder region
     *******************/

    private val _reminders = mutableStateOf(emptyList<Reminder>())
    val reminders: State<List<Reminder>> = _reminders

    private fun getReminders() {
        viewModelScope.launch {
            reminderRepository.getAllReminders().collect() {
                _reminders.value = it
            }
        }
    }

    private val _isCreateReminderDialogShow = mutableStateOf(false)
    val isCreateReminderDialogShow: State<Boolean> = _isCreateReminderDialogShow

    fun switchReminderDialogShow() {
        _isCreateReminderDialogShow.value = !_isCreateReminderDialogShow.value
    }

    fun getReminder(noteId: ObjectId): Reminder? {
        return reminderRepository.getReminderForNote(noteId)
    }

    fun createReminder(date: LocalDate, time: LocalTime, repeatMode: RepeatMode) {
        if (DateHelper.isFutureDateTime(date, time)) {
            viewModelScope.launch {
                reminderRepository.updateOrCreateReminderForNotes(
                    noteRepository.getNotes(selectedNotes.value.toList()),
                    application.applicationContext
                ) { updatedReminder ->
                    updatedReminder.date = date
                    updatedReminder.time = time
                    updatedReminder.repeat = repeatMode
                }
            }
            switchReminderDialogShow()
        }
    }

    fun deleteSelectedReminder() {
        if (selectedNotes.value.isEmpty()) return
        viewModelScope.launch {
            val reminder = reminderRepository.getReminderForNote(selectedNotes.value.toList().first()) ?: return@launch
            reminderRepository.deleteReminder(reminder._id, application.applicationContext)
        }
        switchReminderDialogShow()
    }
}
