package com.jobik.shkiper.widgets.screens.NoteSelectionScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobik.shkiper.database.data.note.NoteMongoRepository
import com.jobik.shkiper.database.data.reminder.ReminderMongoRepository
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.database.models.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

data class NoteSelectScreenState(
    val isNotesInitialized: Boolean = false,
    val notes: List<Note> = emptyList(),
    val searchText: String = "",
    val hashtags: Set<String> = emptySet(),
    val currentHashtag: String? = null,
    val reminders: List<Reminder> = emptyList(),
    val selectedNoteId: String? = null,
)

@HiltViewModel
class NoteSelectionViewModel @Inject constructor(
    private val noteRepository: NoteMongoRepository,
    private val reminderRepository: ReminderMongoRepository,
) : ViewModel() {
    private val _screenState = mutableStateOf(NoteSelectScreenState())
    val screenState: State<NoteSelectScreenState> = _screenState

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
            _screenState.value = _screenState.value.copy(
                notes = noteRepository.getAllNotes().filter { it.position != NotePosition.DELETE },
                isNotesInitialized = true
            )
        }
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

    private fun getNotesByText(newString: String) {
        _screenState.value = screenState.value.copy(currentHashtag = null)
        noteRepository.getAllNotes().let { listNotes ->
            val notes = listNotes.filter {
                it.position != NotePosition.DELETE && (
                        it.body.contains(newString, ignoreCase = true) || it.header.contains(
                            newString,
                            ignoreCase = true
                        ))
            }
            _screenState.value = _screenState.value.copy(notes = notes)
        }
    }

    fun clickOnNote(noteId: ObjectId?) {
        if (noteId == null) {
            _screenState.value = screenState.value.copy(
                selectedNoteId = null
            )
            return
        }
        _screenState.value = screenState.value.copy(
            selectedNoteId = if (screenState.value.selectedNoteId == noteId.toHexString()) null else noteId.toHexString()
        )
    }

    fun getSelectedNote(): Note? {
        if (screenState.value.selectedNoteId == null) return null
        return noteRepository.getNote(ObjectId(screenState.value.selectedNoteId ?: ""))
    }

    fun clearSelectedNote() {
        _screenState.value = screenState.value.copy(
            selectedNoteId = null
        )
    }

    /*******************
     * Hashtag region
     *******************/

    fun getHashtags() {
        viewModelScope.launch {
            _screenState.value = screenState.value.copy(
                hashtags = noteRepository.getAllNotes().filter { it.position != NotePosition.DELETE }
                    .flatMap { it.hashtags }.toSet()
            )
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
            _screenState.value = _screenState.value.copy(
                notes = noteRepository.getAllNotes().filter {
                    it.position != NotePosition.DELETE && it.hashtags.contains(hashtag)
                },
            )
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
}
