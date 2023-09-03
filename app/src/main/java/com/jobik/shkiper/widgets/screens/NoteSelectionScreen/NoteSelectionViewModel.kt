package com.jobik.shkiper.widgets.screens.NoteSelectionScreen

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobik.shkiper.database.data.note.NoteMongoRepository
import com.jobik.shkiper.database.data.reminder.ReminderMongoRepository
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.database.models.Reminder
import com.jobik.shkiper.navigation.Argument_Note_Position
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom
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
    savedStateHandle: SavedStateHandle,
    private val application: Application,
) : ViewModel() {
    private val notePosition: NotePosition
    private val _screenState = mutableStateOf(NoteSelectScreenState())
    val screenState: State<NoteSelectScreenState> = _screenState

    /*******************
     * Notes region
     *******************/

    private var notesFlowJob: Job? = null

    init {
        notePosition = try {
            NotePosition.valueOf(savedStateHandle[Argument_Note_Position] ?: NotePosition.MAIN.name)
        } catch (e: Exception) {
            Log.i("savedStateHandle", "notePositionError", e)
            NotePosition.MAIN
        }
        viewModelScope.launch(Dispatchers.IO) {
            getHashtags()
            getNotes()
            getReminders()
        }
    }

    private fun getNotes() {
        notesFlowJob?.cancel()

        notesFlowJob = viewModelScope.launch {
            noteRepository.getNotesFlow(notePosition).collect() {
                _screenState.value = _screenState.value.copy(notes = it, isNotesInitialized = true)
            }
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

    private suspend fun getNotesByText(newString: String) {
        _screenState.value = screenState.value.copy(currentHashtag = null)
        noteRepository.filterNotesByContains(newString, notePosition).collect {
            _screenState.value = _screenState.value.copy(notes = it)
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
            noteRepository.getHashtags(notePosition).collect() {
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
            noteRepository.getNotesByHashtag(notePosition, hashtag).collect() {
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

    fun getReminder(noteId: ObjectId): Reminder? {
        return reminderRepository.getReminderForNote(noteId)
    }

    private suspend fun showSnackbar(message: String, icon: ImageVector?) {
        SnackbarHostUtil.snackbarHostState.showSnackbar(
            SnackbarVisualsCustom(
                message = message,
                icon = icon
            )
        )
    }
}
