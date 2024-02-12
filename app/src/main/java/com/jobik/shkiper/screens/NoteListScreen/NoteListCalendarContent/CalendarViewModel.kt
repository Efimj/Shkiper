package com.jobik.shkiper.screens.NoteListScreen.NoteListCalendarContent

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.jobik.shkiper.database.data.note.NoteMongoRepository
import com.jobik.shkiper.database.data.reminder.ReminderMongoRepository
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.database.models.Reminder
import com.jobik.shkiper.navigation.AppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CalendarScreenState(
    val notes: List<Note> = emptyList(),
    val reminders: List<Reminder> = emptyList(),
    val hashtags: Set<String> = emptySet(),
    val currentHashtag: String? = null,
)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val noteRepository: NoteMongoRepository,
    private val reminderRepository: ReminderMongoRepository,
    private val application: Application,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _screenState = mutableStateOf(CalendarScreenState())
    val screenState: State<CalendarScreenState> = _screenState

    /*******************
     * Notes region
     *******************/

    private var notesFlowJob: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getNotes()
            getReminders()
            getHashtags()
        }
    }

    private fun getNotes() {
        notesFlowJob?.cancel()

        notesFlowJob = viewModelScope.launch {
            noteRepository.getNotesFlow().collect() {
                _screenState.value = _screenState.value.copy(
                    notes = it.filter { it.position != NotePosition.DELETE },
                )
            }
        }
    }

    fun clickOnNote(note: Note, currentRoute: String, navController: NavController) {
        navController.navigate(AppScreens.Note.noteId(note._id.toHexString())) {
            launchSingleTop = true
        }
    }

    private fun getReminders() {
        viewModelScope.launch {
            reminderRepository.getReminders().collect() {
                _screenState.value = screenState.value.copy(reminders = it)
            }
        }
    }

    /*******************
     * Hashtag region
     *******************/

    fun getHashtags() {
        viewModelScope.launch {
            noteRepository.getHashtags().let {
                _screenState.value = screenState.value.copy(hashtags = it)
            }
        }
    }

    fun setCurrentHashtag(newHashtag: String?) {
        if (newHashtag == screenState.value.currentHashtag) {
            unsetSelectedHashtag()
        } else {
            _screenState.value = screenState.value.copy(currentHashtag = newHashtag)
            getNotesByHashtag(screenState.value.currentHashtag ?: "")
        }
    }

    private fun unsetSelectedHashtag() {
        _screenState.value = screenState.value.copy(currentHashtag = null)
        getNotes()
    }

    fun getNotesByHashtag(hashtag: String) {
        notesFlowJob?.cancel()

//        notesFlowJob = viewModelScope.launch {
//            noteRepository.getNotesByHashtag(hashtag).collect() {
//                _screenState.value = _screenState.value.copy(notes = it)
//                if (it.isEmpty()) unsetSelectedHashtag()
//            }
//        }
    }
}