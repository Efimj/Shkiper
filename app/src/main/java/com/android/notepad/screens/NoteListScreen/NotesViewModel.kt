package com.android.notepad.screens.NoteListScreen

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.notepad.database.data.note.NoteMongoRepository
import com.android.notepad.database.data.reminder.ReminderMongoRepository
import com.android.notepad.database.models.Note
import com.android.notepad.database.models.NotePosition
import com.android.notepad.database.models.Reminder
import com.android.notepad.database.models.RepeatMode
import com.android.notepad.helpers.DateHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    val notes = mutableStateOf(emptyList<Note>())
    var lastCreatedNoteId by mutableStateOf("")
    var searchText by mutableStateOf("")
    private var notesFlowJob: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getHashtags()
            getNotes()
            getReminders()
        }
    }

    private fun getNotes() {
        notesFlowJob?.cancel()

        notesFlowJob = viewModelScope.launch {
            noteRepository.getNotes(NotePosition.MAIN).collect() {
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
        notesFlowJob?.cancel()

        searchText = newString
        notesFlowJob = viewModelScope.launch(Dispatchers.IO) {
            if (newString.isEmpty()) {
                getNotes()
            } else {
                getNotesByText(searchText)
            }
        }
    }

    private suspend fun getNotesByText(newString: String) {
        _currentHashtag.value = null
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
            var unpinnedNote = notes.value.filter { it.isPinned }.find { note ->
                selectedNotes.value.any {
                    it == note._id
                } && !note.isPinned
            }
            if (unpinnedNote == null) {
                unpinnedNote = notes.value.filterNot { it.isPinned }.find { note ->
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
     * Hashtag region
     *******************/
    val hashtags = mutableStateOf(setOf<String>())

    private val _currentHashtag = mutableStateOf<String?>(null)
    val currentHashtag: State<String?> = _currentHashtag

    fun getHashtags() {
        viewModelScope.launch {
            noteRepository.getHashtags(NotePosition.MAIN).collect() {
                hashtags.value = it
            }
        }
    }

    fun setCurrentHashtag(newHashtag: String?) {
        if (newHashtag == _currentHashtag.value) {
            _currentHashtag.value = null
            getNotes()
        } else {
            _currentHashtag.value = newHashtag
            getNotesByHashtag(_currentHashtag.value ?: "")
        }
    }

    fun getNotesByHashtag(hashtag: String) {
        notesFlowJob?.cancel()

        notesFlowJob = viewModelScope.launch {
            noteRepository.getNotesByHashtag(NotePosition.MAIN, hashtag).collect() {
                notes.value = it
            }
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
