package com.android.notepad.screens.NoteListScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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

data class NotesScreenState(
    val isNotesInitialized: Boolean = false,
    val notes: List<Note> = emptyList(),
    val lastCreatedNoteId: String = "",
    val searchText: String = "",
    val selectedNotes: Set<ObjectId> = emptySet(),
    val hashtags: Set<String> = emptySet(),
    val currentHashtag: String? = null,
    val reminders: List<Reminder> = emptyList(),
    val isCreateReminderDialogShow: Boolean = false,
)

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteMongoRepository,
    private val reminderRepository: ReminderMongoRepository,
) : ViewModel() {

    private val _screenState = mutableStateOf(NotesScreenState())
    val screenState: State<NotesScreenState> = _screenState

    /*******************
     * Notes region
     *******************/

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
                _screenState.value = _screenState.value.copy(notes = it, isNotesInitialized = true)
            }
        }
    }

    fun clearSelectedNote() {
        _screenState.value = screenState.value.copy(selectedNotes = setOf())
    }

    fun changeSearchText(newString: String) {
        notesFlowJob?.cancel()

        _screenState.value = _screenState.value.copy(searchText = newString)
        notesFlowJob = viewModelScope.launch(Dispatchers.IO) {
            if (newString.isEmpty()) {
                getNotes()
            } else {
                getNotesByText(_screenState.value.searchText)
            }
        }
    }

    private suspend fun getNotesByText(newString: String) {
        _screenState.value = screenState.value.copy(currentHashtag = null)
        noteRepository.filterNotesByContains(newString).collect {
            _screenState.value = _screenState.value.copy(notes = it)
        }
    }

    fun toggleSelectedNoteCard(noteId: ObjectId) {
        val selectedNotes = screenState.value.selectedNotes
        _screenState.value = screenState.value.copy(
            selectedNotes = if (selectedNotes.contains(noteId)) selectedNotes.minus(noteId)
            else selectedNotes.plus(noteId)
        )
    }

    fun deleteSelectedNotes() {
        viewModelScope.launch {
            noteRepository.deleteNote(screenState.value.selectedNotes.toList())
            clearSelectedNote()
        }
    }

    fun pinSelectedNotes() {
        viewModelScope.launch {
            val pinMode: Boolean
            var unpinnedNote = _screenState.value.notes.filter { it.isPinned }.find { note ->
                screenState.value.selectedNotes.any {
                    it == note._id
                } && !note.isPinned
            }
            if (unpinnedNote == null) {
                unpinnedNote = _screenState.value.notes.filterNot { it.isPinned }.find { note ->
                    screenState.value.selectedNotes.any {
                        it == note._id
                    } && !note.isPinned
                }
            }
            pinMode = unpinnedNote != null
            noteRepository.updateNote(screenState.value.selectedNotes.toList()) { updatedNote ->
                updatedNote.isPinned = pinMode
            }
            clearSelectedNote()
        }
    }

    fun createNewNote() {
        viewModelScope.launch(Dispatchers.IO) {
            val newNote = Note()
            noteRepository.insertNote(newNote)
            _screenState.value = screenState.value.copy(lastCreatedNoteId = newNote._id.toHexString())
        }
    }

    fun clearLastCreatedNote() {
        _screenState.value = screenState.value.copy(lastCreatedNoteId = "")
    }

    /*******************
     * Hashtag region
     *******************/

    fun getHashtags() {
        viewModelScope.launch {
            noteRepository.getHashtags(NotePosition.MAIN).collect() {
                _screenState.value = screenState.value.copy(hashtags = it)
            }
        }
    }

    fun setCurrentHashtag(newHashtag: String?) {
        if (newHashtag == screenState.value.currentHashtag) {
            _screenState.value = screenState.value.copy(currentHashtag = null)
            getNotes()
        } else {
            _screenState.value = screenState.value.copy(currentHashtag = newHashtag)
            getNotesByHashtag(screenState.value.currentHashtag ?: "")
        }
    }

    fun getNotesByHashtag(hashtag: String) {
        notesFlowJob?.cancel()

        notesFlowJob = viewModelScope.launch {
            noteRepository.getNotesByHashtag(NotePosition.MAIN, hashtag).collect() {
                _screenState.value = _screenState.value.copy(notes = it)
            }
        }
    }

    /*******************
     * Reminder region
     *******************/

    private fun getReminders() {
        viewModelScope.launch {
            reminderRepository.getReminders().collect() {
                _screenState.value = screenState.value.copy(reminders = it)
            }
        }
    }

    fun switchReminderDialogShow() {
        _screenState.value =
            _screenState.value.copy(isCreateReminderDialogShow = !_screenState.value.isCreateReminderDialogShow)
    }

    fun getReminder(noteId: ObjectId): Reminder? {
        return reminderRepository.getReminderForNote(noteId)
    }

    fun createReminder(date: LocalDate, time: LocalTime, repeatMode: RepeatMode) {
        if (DateHelper.isFutureDateTime(date, time)) {
            viewModelScope.launch {
                reminderRepository.updateOrCreateReminderForNotes(
                    noteRepository.getNotes(screenState.value.selectedNotes.toList()),
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
        if (screenState.value.selectedNotes.isEmpty()) return
        viewModelScope.launch {
            val reminder =
                reminderRepository.getReminderForNote(screenState.value.selectedNotes.toList().first()) ?: return@launch
            reminderRepository.deleteReminder(reminder._id)
        }
        switchReminderDialogShow()
    }
}
